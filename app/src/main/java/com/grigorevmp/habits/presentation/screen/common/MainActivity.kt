package com.grigorevmp.habits.presentation.screen.common

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.grigorevmp.habits.R
import com.grigorevmp.habits.core.alarm.createChannel
import com.grigorevmp.habits.data.repository.PreferencesRepository
import com.grigorevmp.habits.domain.usecase.synchronizer.UpdateNotificationUseCase
import com.grigorevmp.habits.domain.usecase.synchronizer.UpdateSyncPointUseCase
import com.grigorevmp.habits.presentation.screen.habits.HabitsViewModel
import com.grigorevmp.habits.presentation.screen.settings.SettingsScreenViewModel
import com.grigorevmp.habits.presentation.screen.today.TodayScreenViewModel
import com.grigorevmp.habits.presentation.theme.HabitTrackerTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var updateSyncPointUseCase: UpdateSyncPointUseCase

    @Inject
    lateinit var updateNotificationUseCase: UpdateNotificationUseCase

    @Inject
    lateinit var preferencesRepository: PreferencesRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        synchronize()
        checkPermission()

        setContent {
            MainScreenView(viewModel(), viewModel(), viewModel())
        }
    }

    private fun synchronize() {
        updateSyncPointUseCase.invoke()
        updateNotificationUseCase.invoke(this)

        createChannel(this)
    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    fun MainScreenView(
        todayScreenViewModel: TodayScreenViewModel,
        habitViewModel: HabitsViewModel,
        settingsViewModel: SettingsScreenViewModel,
    ) {
        val navController = rememberNavController()

        HabitTrackerTheme {
            Scaffold(bottomBar = { BottomNavigation(navController = navController) }) {
                Box(modifier = Modifier.padding(it)) {
                    NavigationGraph(
                        navController = navController,
                        todayScreenViewModel = todayScreenViewModel,
                        habitViewModel = habitViewModel,
                        settingsViewModel = settingsViewModel,
                    )
                }
            }
        }
    }

    @Composable
    fun NavigationGraph(
        navController: NavHostController,
        todayScreenViewModel: TodayScreenViewModel,
        settingsViewModel: SettingsScreenViewModel,
        habitViewModel: HabitsViewModel,
    ) {
        val context = LocalContext.current

        NavHost(
            navController,
            startDestination = BottomNavItem.Today(context.getString(R.string.bottom_nav_home)).screenRoute
        ) {
            composable(BottomNavItem.Today(context.getString(R.string.bottom_nav_home)).screenRoute) {
                TodayNavScreen(todayScreenViewModel)
            }

            composable(BottomNavItem.Habits(context.getString(R.string.bottom_nav_habits)).screenRoute) {
                HabitListNavScreen(habitViewModel)
            }

            composable(BottomNavItem.Settings(context.getString(R.string.bottom_nav_settings)).screenRoute) {
                SettingsNavScreen(settingsViewModel)
            }
        }
    }

    @Composable
    fun BottomNavigation(navController: NavController) {
        val context = LocalContext.current

        val items = listOf(
            BottomNavItem.Today(context.getString(R.string.bottom_nav_home)),
            BottomNavItem.Habits(context.getString(R.string.bottom_nav_habits)),
            BottomNavItem.Settings(context.getString(R.string.bottom_nav_settings)),
        )
        NavigationBar {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            items.forEach { item ->
                NavigationBarItem(icon = {
                    Icon(painterResource(id = item.icon), contentDescription = item.title)
                },
                    label = {
                        Text(
                            text = item.title, fontSize = 9.sp
                        )
                    },
                    alwaysShowLabel = true,
                    selected = currentRoute == item.screenRoute,
                    onClick = {
                        navController.navigate(item.screenRoute) {

                            navController.graph.startDestinationRoute?.let { screenRoute ->
                                popUpTo(screenRoute) {
                                    saveState = true
                                }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    })
            }
        }
    }

    @SuppressLint("BatteryLife")
    private fun checkPermission() {
        if (!preferencesRepository.getPermissionShown()) {
            val requestPermissionLauncher = registerForActivityResult(
                ActivityResultContracts.RequestPermission(),
            ) { _: Boolean -> }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }

            val packageName: String = packageName
            val pm: PowerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                val intent = Intent()
                intent.action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
                intent.flags = FLAG_ACTIVITY_NEW_TASK
                intent.data = Uri.parse("package:$packageName")
                startActivity(intent)
            }

            preferencesRepository.setPermissionShown()
        }
    }
}

