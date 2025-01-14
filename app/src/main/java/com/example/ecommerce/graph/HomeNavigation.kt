package com.example.ecommerce.graph

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ListAlt
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.ecommerce.main.home.HomeScreen
import com.example.ecommerce.main.store.StoreScreen
import com.example.ecommerce.main.transaction.TransactionScreen
import com.example.ecommerce.main.wishlist.WishListScreen

@OptIn(
    ExperimentalMaterialApi::class,
    ExperimentalMaterial3Api::class,
    ExperimentalLayoutApi::class
)
@Composable
fun HomeNavigation(
    navController: NavHostController,
    onLogoutClick: () -> Unit,
    onDetailClick: (id: String) -> Unit,
    onNavigateToStatus: (fulfillment: String) -> Unit
) {
    NavHost(
        navController = navController,
        route = Graph.Main.route,
        startDestination = Bottom.Home.route
    ) {
        composable(Bottom.Home.route) {
            HomeScreen(
                onLogoutClick = {
                    onLogoutClick()
                }
            )
        }
        composable(Bottom.Store.route) {
            StoreScreen(
                onDetailClick = { id ->
                    onDetailClick(id)
                }
            )
        }
        composable(Bottom.Wishlist.route) {
            WishListScreen()
        }

        composable(Bottom.Transaction.route) {
            TransactionScreen(
                onNavigateToStatus = { fulfillment ->
                    onNavigateToStatus(fulfillment)
                }
            )
        }
    }
}

sealed class Bottom(
    var title: String,
    var selectedIcon: ImageVector,
    var unselectedIcon : ImageVector,
    var route: String
) {
    object Home : Bottom("Home", Icons.Default.Home, Icons.Filled.Home,"home")
    object Store : Bottom("Store", Icons.Default.Store, Icons.Filled.Store,"store")
    object Wishlist : Bottom("Wishlist", Icons.Default.Favorite, Icons.Filled.Favorite,"wishlist")
    object Transaction : Bottom("Transaction",
        Icons.AutoMirrored.Default.ListAlt, Icons.AutoMirrored.Filled.ListAlt,"transaction")
}
