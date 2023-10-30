package com.grigorevmp.habits.presentation.screen.common

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.grigorevmp.habits.domain.usecase.date_synchronizer.UpdateSyncPointUseCase
import com.grigorevmp.habits.presentation.screen.today.HabitViewModel
import com.grigorevmp.habits.core.alarm.createChannel
import com.grigorevmp.habits.presentation.theme.HabitTrackerTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var updateSyncPointUseCase: UpdateSyncPointUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        updateSyncPointUseCase.invoke()
        createChannel(this)
        checkPermission()
    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    fun MainScreenView(habitViewModel: HabitViewModel? = null) {
        val navController = rememberNavController()

        HabitTrackerTheme {
            Scaffold(bottomBar = { BottomNavigation(navController = navController) }) {
                Box(modifier = Modifier.padding(it)) {
                    NavigationGraph(
                        navController = navController, habitViewModel = habitViewModel
                    )
                }
            }
        }
    }

    @Composable
    fun NavigationGraph(navController: NavHostController, habitViewModel: HabitViewModel? = null) {
        NavHost(navController, startDestination = BottomNavItem.Today.screenRoute) {
            composable(BottomNavItem.Today.screenRoute) {
                TodayScreen(habitViewModel)
            }
            composable(BottomNavItem.Habits.screenRoute) {
                HabitListScreen(habitViewModel)
            }
            composable(BottomNavItem.Settings.screenRoute) {
                SettingsScreen()
            }
        }
    }

    @Composable
    fun BottomNavigation(navController: NavController) {
        val items = listOf(
            BottomNavItem.Today,
            BottomNavItem.Habits,
            BottomNavItem.Settings,
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

                            navController.graph.startDestinationRoute?.let { screen_route ->
                                popUpTo(screen_route) {
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

    private fun checkPermission() {
        val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission(),
        ) { isGranted: Boolean ->
            if (isGranted) {

            } else {

            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {

            } else {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        setContent {
            val viewModel: HabitViewModel = viewModel()
            MainScreenView(viewModel)
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun MainNavigationPreview() {
        MainScreenView()
    }
}

