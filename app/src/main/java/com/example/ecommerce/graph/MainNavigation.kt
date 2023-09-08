package com.example.ecommerce.graph

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.ecommerce.auth.profile.ProfileRoute
import com.example.ecommerce.main.cart.CartScreen
import com.example.ecommerce.main.checkout.CheckoutScreen
import com.example.ecommerce.main.detail.DetailScreen
import com.example.ecommerce.main.main.MainScreen
import com.example.ecommerce.main.payment.PaymentScreen
import com.example.ecommerce.main.review.ReviewScreen
import com.example.ecommerce.main.status.StatusScreen
import com.example.ecommerce.main.store.SearchScreen

@ExperimentalMaterial3Api
fun NavGraphBuilder.mainNavGraph(
    navController: NavHostController
) {
    navigation(
        route = Graph.Main.route,
        startDestination = Main.Home.route
    ) {
        composable(route = Main.Profile.route) {
            ProfileRoute(
                onNavigateToHome= {
                    navController.navigate(Main.Home.route){
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(route = Main.Home.route) {
            MainScreen(navController)
        }

        composable(route = Main.Detail.route,
            arguments = listOf(navArgument("id") {type = NavType.StringType})
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            DetailScreen(navController,id){ productId->
                navController.navigate("Review/$productId")
            }
        }

        composable(route = Main.Review.route,
            arguments = listOf(navArgument("id") {type = NavType.StringType})){
                backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            ReviewScreen(navController,id)
        }

        composable(route = Main.Cart.route){
            CartScreen(navController){
                navController.navigate(Main.Checkout.route)
            }
        }

        composable(route = Main.Checkout.route){
            CheckoutScreen(
                navController,
                choosePayment = {
                    navController.navigate(Main.Payment.route)
                },
                productPayment = {
                    navController.navigate(Main.Status.route)
                }
            )
        }

        composable(route = Main.Payment.route){
            PaymentScreen(navController)
        }

        composable(route = Main.Status.route){
            StatusScreen(navController)
        }
    }
}

sealed class Main(val route: String) {
    object Profile : Main(route = "Profile")
    object Home : Main(route = "Home")
    object Detail : Main("Detail/{id}")
    object Review : Main("Review/{id}")
    object Cart : Main("Cart")
    object Checkout : Main("Checkout")
    object Payment : Main("Payment")
    object Status : Main("Status")
}