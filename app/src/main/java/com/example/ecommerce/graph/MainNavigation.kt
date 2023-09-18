package com.example.ecommerce.graph

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.ecommerce.api.model.Fulfillment
import com.example.ecommerce.api.model.FulfillmentType
import com.example.ecommerce.api.model.Item
import com.example.ecommerce.api.model.ProductType
import com.example.ecommerce.auth.profile.ProfileRoute
import com.example.ecommerce.main.cart.CartScreen
import com.example.ecommerce.main.checkout.CheckoutScreen
import com.example.ecommerce.main.detail.DetailScreen
import com.example.ecommerce.main.main.MainScreen
import com.example.ecommerce.main.notification.NotificationScreen
import com.example.ecommerce.main.payment.PaymentScreen
import com.example.ecommerce.main.review.ReviewScreen
import com.example.ecommerce.main.status.StatusScreen
import com.example.ecommerce.room.cart.ListCheckout

@ExperimentalMaterial3Api
fun NavGraphBuilder.mainNavGraph(
    navController: NavHostController,
    startDestination : String) {
    navigation(route = Graph.Main.route,
        startDestination = startDestination
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
            DetailScreen(
                navController,
                id,
                onReviewClick = { productId->
                    navController.navigate("Review/$productId")
                },
                onCheckOut = { listCheck ->
                    navController.navigate("Checkout/$listCheck")
                }
            )
        }

        composable(route = Main.Review.route,
            arguments = listOf(navArgument("id") {type = NavType.StringType})
        ){ backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            ReviewScreen(navController,id)
        }

        composable(route = Main.Cart.route){
            CartScreen(navController){ listCheck ->
                navController.navigate("Checkout/$listCheck")
            }
        }

        composable(route = Main.Checkout.route,
            arguments = listOf(
                navArgument("listCheckout") { type = ProductType()}
            ),
        ){ backStackEntry ->
            val checkoutItem = backStackEntry.arguments?.getParcelable<ListCheckout>("listCheckout")
            val paymentItem = backStackEntry.savedStateHandle.get<Item>("payment")
            CheckoutScreen(
                navController,
                checkoutItem,
                choosePayment = {
                    navController.navigate(Main.Payment.route)
                },
                productPayment = { fulfillment ->
                    navController.navigate("Status/$fulfillment")
                },
                paymentItem = paymentItem
            )
        }

        composable(route = Main.Payment.route){
            PaymentScreen(
                onNavigateBack = {
                    navController.navigateUp()
                },
                onItemClick = { paymentItem ->
                    navController
                        .previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("payment", paymentItem)
                    navController.popBackStack()
                }
            )
        }

        composable(route = Main.Status.route,
            arguments = listOf(navArgument("fulfillment") { type = FulfillmentType()})
        ){
            val fulfillmentItem = it.arguments?.getParcelable<Fulfillment>("fulfillment")
            StatusScreen(
                navController,
                fulfillmentItem,
                onNavigateToHome = {
                    navController.navigate(Main.Home.route)
                }
            )
        }

        composable(route = Main.Notification.route){
            NotificationScreen(navController = navController)
        }
    }
}

sealed class Main(val route: String) {
    object Profile : Main(route = "Profile")
    object Home : Main(route = "Home")
    object Detail : Main("Detail/{id}")
    object Review : Main("Review/{id}")
    object Cart : Main("Cart")
    object Checkout : Main("Checkout/{listCheckout}")
    object Payment : Main("Payment")
    object Status : Main("Status/{fulfillment}")
    object Notification : Main("Notification")
}