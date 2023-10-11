package com.example.ecommerce.graph

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.ecommerce.auth.login.LoginRoute
import com.example.ecommerce.auth.register.RegisterRoute
import com.example.ecommerce.boarding.onBoardingRoute
import com.example.ecommerce.main.main.MainViewModel

@ExperimentalMaterial3Api
@Composable
fun RootNavigationGraph(
    navController: NavHostController,
    windowSizeClass: WindowSizeClass
) {
    val mainViewModel: MainViewModel = hiltViewModel()
    val isLogin = mainViewModel.getLoginState()
    val isBoarding = mainViewModel.getBoardingState()
    val isProfile = mainViewModel.getProfileName()

    NavHost(
        navController = navController,
        route = Graph.Root.route,
        startDestination = if (isLogin) {
            Graph.Main.route
        } else Graph.Authentication.route
    ) {
        navigation(
            route = Graph.Authentication.route,
            startDestination = if (isBoarding) {
                Authentication.Login.route
            } else
                Authentication.OnBoarding.route
        ) {
            composable(route = Authentication.OnBoarding.route) {
                onBoardingRoute(
                    onNavigateToRegister = {
                        navController.navigate(Authentication.Register.route)
                    },
                    onNavigateToLogin = {
                        navController.navigate(Authentication.Login.route)
                    }
                )
            }
            composable(route = Authentication.Login.route) {
                LoginRoute(
                    onNavigateToHome = {
                        navController.navigate(Graph.Main.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                inclusive = true
                            }
                        }
                    },
                    onNavigateToRegister = {
                        navController.navigate(Authentication.Register.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                inclusive = true
                            }
                        }
                    },
                    onNavigateToBoarding = {
                        navController.navigate(Authentication.OnBoarding.route)
                    }
                )
            }
            composable(route = Authentication.Register.route) {
                RegisterRoute(
                    onNavigateToHome = {
                        navController.navigate(Graph.Main.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                inclusive = true
                            }
                        }
                    },
                    onNavigateToLogin = {
                        navController.navigate(Authentication.Login.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                inclusive = true
                            }
                        }
                    }
                )
            }
        }

        mainNavGraph(navController = navController, isProfile = isProfile, windowSizeClass)
    }
}

sealed class Graph(val route: String) {
    object Root : Graph(route = "Root")
    object Authentication : Graph(route = "Authentication")
    object Main : Graph(route = "Main")
    object ChildMain : Graph(route = "ChildMain")
}
