package com.example.ecommerce.graph

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.ecommerce.auth.login.LoginRoute
import com.example.ecommerce.auth.register.RegisterRoute
import com.example.ecommerce.boarding.onBoardingRoute

@ExperimentalMaterial3Api
fun NavGraphBuilder.authNavGraph(
    navController: NavHostController,
    startLogin: String
) {
    navigation(
        route = Graph.Authentication.route,
        startDestination = startLogin,
    ) {
        composable(route = Authentication.OnBoarding.route) {
            onBoardingRoute(
                onNavigateToRegister = {
                    navController.navigate(Authentication.Register.route) {
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
                    navController.navigate(Authentication.OnBoarding.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                    }
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
}

sealed class Authentication(val route: String) {
    object OnBoarding : Authentication(route = "OnBoarding")
    object Login : Authentication(route = "Login")
    object Register : Authentication(route = "Register")
}
