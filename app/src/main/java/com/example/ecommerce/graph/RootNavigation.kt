package com.example.ecommerce.graph

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.ecommerce.main.main.MainScreen
import com.example.ecommerce.boarding.onBoardingRoute

@ExperimentalMaterial3Api
@Composable
fun RootNavigationGraph(
    navController: NavHostController,
    startDestination: String,
) {
    NavHost(navController = navController,
        route = Graph.Root.route,
        startDestination = startDestination
    ) {
        composable(route = Graph.OnBoarding.route){
            onBoardingRoute(
                onNavigateToRegister= {
                    navController.navigate(Authentication.Register.route)
                },
                onNavigateToLogin = {
                    navController.navigate(Authentication.Login.route)
                }
            )
        }
        authNavGraph(navController = navController)
        mainNavGraph(navController = navController)
    }
}

sealed class Graph(val route: String) {
    object Root : Graph(route = "Root")
    object OnBoarding : Graph(route = "onBoarding")
    object Authentication : Graph(route = "Authentication")
    object Main : Graph(route = "Main")
}
