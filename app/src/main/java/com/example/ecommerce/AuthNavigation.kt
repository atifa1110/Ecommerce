package com.example.ecommerce

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.ecommerce.auth.LoginRoute
import com.example.ecommerce.auth.RegisterRoute

@ExperimentalMaterial3Api
fun NavGraphBuilder.authNavGraph(navController: NavHostController) {
    navigation(
        route = Graph.Authentication.route,
        startDestination = Authentication.Login.route
    ) {
        composable(route = Authentication.Login.route) {
            LoginRoute(
                onLoginSubmitted = {
                    navController.popBackStack()
                    navController.navigate(Graph.Home.route)
                },
                onRegisterClick = {
                    navController.popBackStack()
                    navController.navigate(Authentication.Register.route)
                }
            )
        }
        composable(route = Authentication.Register.route) {
            RegisterRoute(
                onRegisterSubmitted = {
                    navController.popBackStack()
                    navController.navigate(Graph.Home.route)
                },
                onLoginClick = {
                    navController.popBackStack()
                    navController.navigate(Authentication.Login.route)
                }
            )
        }
    }
}

sealed class Authentication(val route: String) {
    object Login : Authentication(route = "Login")
    object Register : Authentication(route = "Register")
}