package com.example.ecommerce

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ecommerce.onBoarding.onBoardingRoute
import com.example.ecommerce.onBoarding.onBoardingScreen
import com.google.accompanist.pager.ExperimentalPagerApi

@OptIn(ExperimentalAnimationApi::class, ExperimentalPagerApi::class)
@ExperimentalMaterial3Api
@Composable
fun RootNavigationGraph(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController,
        route = Graph.Root.route,
        startDestination = Graph.onBoarding.route) {
        composable(route = Graph.onBoarding.route){
            onBoardingRoute(
                onJoinClick = {
                    navController.popBackStack()
                    navController.navigate(Authentication.Register.route)
                },
                onSkipClick = {
                    navController.popBackStack()
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
