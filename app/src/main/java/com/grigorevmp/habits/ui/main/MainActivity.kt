package com.grigorevmp.habits.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.grigorevmp.habits.domain.usecase.date.UpdateSyncPointUseCase
import com.grigorevmp.habits.ui.home.HabitViewModel
import com.grigorevmp.habits.ui.theme.HabitTrackerTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var updateSyncPointUseCase: UpdateSyncPointUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        updateSyncPointUseCase.invoke()

        setContent {
            val viewModel: HabitViewModel = viewModel()
            MainScreenView(viewModel)
        }
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
        NavHost(navController, startDestination = BottomNavItem.Home.screenRoute) {
            composable(BottomNavItem.Home.screenRoute) {
                HomeScreen(habitViewModel)
            }
            composable(BottomNavItem.Habits.screenRoute) {
                HabitListScreen(habitViewModel)
            }
        }
    }

    @Composable
    fun BottomNavigation(navController: NavController) {
        val items = listOf(
            BottomNavItem.Home,
            BottomNavItem.Habits,
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

    @Preview(showBackground = true)
    @Composable
    fun MainNavigationPreview() {
        MainScreenView()
    }
}

