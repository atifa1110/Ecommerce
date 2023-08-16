package com.example.ecommerce

import androidx.compose.foundation.Image
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController


@Composable
fun BottomNavigationCustom(navController: NavHostController) {
    val items = listOf(Bottom.Home, Bottom.History, Bottom.Profile)

    var selectedItem = remember { mutableStateOf(0) }

    // first create navController object.
    var navController = rememberNavController()

    // get navBackStackEntry as State so we can refresh the ui onBackStack event.
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val parentRouteName = navBackStackEntry.value?.destination?.parent?.route

    //Get current page name from backStackEntry
    val routeName = navBackStackEntry.value?.destination?.route

    NavigationBar() {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination
        items.forEach { item ->
            currentRoute?.let {
                NavigationBarItem(
                    selected = it.hierarchy.any { nav->
                        nav.route == item.route },
//                    selected = item.route == routeName,
                    onClick = {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.findStartDestination().id)
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    label = { Text(text = item.title)},
                    icon = { Image(imageVector = item.icon, contentDescription = item.title)
                    })
            }
        }
    }
}