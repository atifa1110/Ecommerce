package com.example.ecommerce.graph

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ecommerce.main.MainScreen
import com.example.ecommerce.boarding.onBoardingRoute
import com.google.accompanist.pager.ExperimentalPagerApi

@ExperimentalMaterial3Api
@Composable
fun RootNavigationGraph(navController: NavHostController, startDestination: String) {
    NavHost(navController = navController,
        startDestination = Graph.onBoarding.route) {
        composable(route = Graph.onBoarding.route){
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
        composable(route = Graph.Home.route) {
            MainScreen(navController)
        }
    }
}

sealed class Graph(val route: String) {
    object Root : Graph(route = "Root")
    object onBoarding : Graph(route = "onBoarding")
    object Authentication : Graph(route = "Authentication")
    object Home : Graph(route = "Home")
}
