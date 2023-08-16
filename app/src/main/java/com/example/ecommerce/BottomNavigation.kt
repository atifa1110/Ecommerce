package com.example.ecommerce

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun BottomNavigation (navController: NavHostController) {
    NavHost(navController = navController,
        startDestination = Bottom.Home.route){
        composable(Bottom.Home.route){

        }
        composable(Bottom.History.route){

        }
        composable(Bottom.Profile.route){

        }
    }
}

sealed class Bottom(var title: String, var icon: ImageVector, var route:String){
    object Home : Bottom("Home", Icons.Default.Home,"home")
    object History : Bottom("History",Icons.Default.Favorite,"history")
    object Profile : Bottom("Profile",Icons.Default.AccountCircle,"profile")
}