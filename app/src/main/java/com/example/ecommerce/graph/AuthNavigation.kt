package com.example.ecommerce.graph

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.ecommerce.auth.login.LoginRoute
import com.example.ecommerce.auth.register.RegisterRoute

@ExperimentalMaterial3Api
fun NavGraphBuilder.authNavGraph(navController: NavHostController) {
    navigation(
        route = Graph.Authentication.route,
        startDestination = Authentication.Login.route
    ) {
        composable(route = Authentication.Login.route) {
            LoginRoute(
                onLoginSubmitted = {
                    navController.navigate(Bottom.Home.route){
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                onRegisterClick = {
                    navController.navigate(Authentication.Register.route){
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(route = Authentication.Register.route) {
            RegisterRoute(
                onRegisterSubmitted = {
                    navController.navigate(Authentication.Profile.route)
                },
                onLoginClick = {
                    navController.navigate(Authentication.Login.route)
                }
            )
        }
    }
}

sealed class Authentication(val route: String) {
    object Login : Authentication(route = "Login")
    object Register : Authentication(route = "Register")
    object Profile : Authentication(route = "Profile")
}