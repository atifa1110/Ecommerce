package com.example.ecommerce.graph

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.Store
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.ecommerce.ScreenContent
import com.example.ecommerce.main.home.HomeRoute

@Composable
fun HomeNavigation (navController: NavHostController,logout: () -> Unit) {
    NavHost(navController = navController,
        route = Graph.Home.route,
        startDestination = Bottom.Home.route){
        composable(Bottom.Home.route){
           HomeRoute {
               logout
           }
        }
        composable(Bottom.Store.route){
            ScreenContent(
                name = Bottom.Store.route,
                onClick = {})
        }
        composable(Bottom.Wishlist.route){
            ScreenContent(
                name = Bottom.Wishlist.route,
                onClick = {})
        }
        composable(Bottom.Transaction.route){
            ScreenContent(
                name = Bottom.Transaction.route,
                onClick = {})
        }
    }
}

sealed class Bottom(var title: String, var icon: ImageVector, var route:String){
    object Home : Bottom("Home", Icons.Default.Home,"home")
    object Store : Bottom("Store",Icons.Default.Store, "store")
    object Wishlist : Bottom("Wishlist",Icons.Default.Favorite,"wishlist")
    object Transaction : Bottom("Transaction",Icons.Default.ListAlt,"transaction")
}