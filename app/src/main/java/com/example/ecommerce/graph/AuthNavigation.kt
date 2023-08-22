package com.example.ecommerce.graph

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.ecommerce.auth.login.LoginRoute
import com.example.ecommerce.auth.profile.ProfileRoute
import com.example.ecommerce.auth.register.RegisterRoute

@ExperimentalMaterial3Api
fun NavGraphBuilder.authNavGraph(navController: NavHostController) {
    navigation(
        route = Graph.Authentication.route,
        startDestination = Authentication.Login.route
    ) {
        composable(route = Authentication.Login.route) {
            LoginRoute(
                onNavigateToHome= {
                    navController.navigate(Bottom.Home.route){
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                onNavigateToRegister = {
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
                onNavigateToProfile = { email, password ->
                    //parsing email and password to profile page
                    navController.navigate("Profile/$email/$password")
                },
                onNavigateToLogin= {
                    navController.navigate(Authentication.Login.route){
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(route = Authentication.Profile.route,
            arguments = listOf(navArgument("email") { type = NavType.StringType },
                navArgument("password"){type = NavType.StringType }
            )
        ) {
            //get email and password argument from register page
            val email = it.arguments?.getString("email")
            val password = it.arguments?.getString("password")
            ProfileRoute(
                email,
                password,
                onNavigateToHome= {
                    navController.navigate(Graph.Home.route){
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}

sealed class Authentication(val route: String) {
    object Login : Authentication(route = "Login")
    object Register : Authentication(route = "Register")
    object Profile : Authentication(route = "Profile/{email}/{password}")
}