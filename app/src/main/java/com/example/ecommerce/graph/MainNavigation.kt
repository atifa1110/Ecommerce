package com.example.ecommerce.graph

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.core.api.model.Fulfillment
import com.example.core.api.model.FulfillmentType
import com.example.core.api.model.Item
import com.example.core.api.model.ProductType
import com.example.core.room.cart.ListCheckout
import com.example.ecommerce.NavigationType
import com.example.ecommerce.auth.profile.ProfileRoute
import com.example.ecommerce.main.cart.CartScreen
import com.example.ecommerce.main.checkout.CheckoutScreen
import com.example.ecommerce.main.detail.DetailScreen
import com.example.ecommerce.main.main.MainScreen
import com.example.ecommerce.main.notification.NotificationScreen
import com.example.ecommerce.main.payment.PaymentScreen
import com.example.ecommerce.main.review.ReviewScreen
import com.example.ecommerce.main.status.StatusScreen
import com.example.screen.ModularScreen

@ExperimentalMaterial3Api
fun NavGraphBuilder.mainNavGraph(
    navController: NavHostController,
    isProfile: String,
    navigationType: NavigationType
) {
    navigation(
        route = Graph.Main.route,
        startDestination = if (isProfile.isNotEmpty()) Main.Home.route else Main.Profile.route
    ) {
        composable(route = Main.Profile.route) {
            ProfileRoute(
                onNavigateToHome = {
                    navController.navigate(Main.Home.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(route = Main.Home.route) {
            MainScreen(
                onLogoutClick = {
                    navController.navigate(Graph.Authentication.route) {
                        popUpTo(Graph.Main.route) {
                            inclusive = true
                        }
                    }
                },
                onDetailClick = { id ->
                    navController.navigate("Detail/$id")
                },
                onNavigateToStatus = { transaction ->
                    navController.navigate("Status/$transaction")
                },
                onNavigateToCart = {
                    navController.navigate(Main.Cart.route)
                },
                onNavigateToNotification = {
                    navController.navigate(Main.Notification.route)
                },
                onNavigateToModular = {
                    navController.navigate(Main.Modular.route)
                },
                navigationType = navigationType
            )
        }

        composable(
            route = Main.Detail.route,
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            DetailScreen(
                id,
                onReviewClick = { productId ->
                    navController.navigate("Review/$productId")
                },
                onCheckOut = { listCheck ->
                    navController.navigate("Checkout/$listCheck")
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Main.Review.route,
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            ReviewScreen(
                id,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(route = Main.Cart.route) {
            CartScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onCheckOut = { listCheck ->
                    navController.navigate("Checkout/$listCheck")
                }
            )
        }

        composable(
            route = Main.Checkout.route,
            arguments = listOf(
                navArgument("listCheckout") { type = ProductType() }
            ),
        ) { backStackEntry ->
            val checkoutItem = backStackEntry.arguments?.getParcelable<ListCheckout>("listCheckout")
            val paymentItem = backStackEntry.savedStateHandle.get<Item>("payment")
            CheckoutScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                checkoutItem,
                choosePayment = {
                    navController.navigate(Main.Payment.route)
                },
                onClickToPayment = { fulfillment ->
                    navController.navigate("Status/$fulfillment")
                },
                paymentItem = paymentItem
            )
        }

        composable(route = Main.Payment.route) {
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

        composable(
            route = Main.Status.route,
            arguments = listOf(navArgument("fulfillment") { type = FulfillmentType() })
        ) {
            val fulfillmentItem = it.arguments?.getParcelable<Fulfillment>("fulfillment")
            StatusScreen(
                fulfillmentItem,
                onNavigateToHome = {
                    navController.navigate(Bottom.Home.route) {
                        popUpTo(Main.Home.route)
                    }
                }
            )
        }

        composable(route = Main.Notification.route) {
            NotificationScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(route = Main.Modular.route) {
            ModularScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
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
    object Modular : Main("Modular")
}
