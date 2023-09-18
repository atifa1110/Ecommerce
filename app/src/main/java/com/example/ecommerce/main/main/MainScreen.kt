package com.example.ecommerce.main.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Badge
import androidx.compose.material.BadgedBox
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Reorder
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationDrawerItemColors
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ecommerce.R
import com.example.ecommerce.graph.Bottom
import com.example.ecommerce.graph.BottomNavigation
import com.example.ecommerce.graph.Graph
import com.example.ecommerce.graph.Main
import com.example.ecommerce.ui.theme.PurplePink

@ExperimentalMaterial3Api
@Composable
fun MainScreen(navController: NavHostController) {
    val navBarController = rememberNavController()
    val mainViewModel : MainViewModel = hiltViewModel()
    val cartSize = mainViewModel.cartSize.collectAsStateWithLifecycle(emptyList()).value.size
    val badgeCart = if(cartSize==0) 0 else cartSize

    val favoriteSize = mainViewModel.favoriteSize.collectAsStateWithLifecycle(emptyList()).value.size
    val badgeFavorite = if(favoriteSize==0) 0 else favoriteSize

    val profile = mainViewModel.getProfileName().collectAsStateWithLifecycle("")
    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text(text = profile.value,
                            style = MaterialTheme.typography.titleLarge
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {}) {
                            Icon(Icons.Default.AccountCircle, "account_profile")
                        }
                    },
                    actions = {
                        IconButton(onClick = { navController.navigate(Main.Notification.route) }) {
                            Icon(Icons.Filled.Notifications, contentDescription = null)
                        }
                        IconButton(onClick = { navController.navigate(Main.Cart.route) }) {
                            if (cartSize > 0) {
                                BadgedBox(
                                    badge = {
                                        Badge {
                                            Text(
                                                text = badgeCart.toString(),
                                                color = Color.White
                                            )
                                        }
                                    }) {
                                    Icon(
                                        Icons.Filled.ShoppingCart,
                                        contentDescription = "Cart"
                                    )
                                }
                            } else {
                                Icon(Icons.Filled.ShoppingCart, contentDescription = "Cart")
                            }
                        }
                        IconButton(onClick = {}) {
                            Icon(Icons.Filled.Reorder, contentDescription = null)
                        }
                    }
                )
                Divider()
            }
        },
        bottomBar = {
            BottomBar(navController = navBarController,badgeFavorite)
        }
    ) {
        Box(modifier = Modifier.padding(it)) {
            BottomNavigation(
                navController = navBarController,
                onLogoutClick = {
                    navController.navigate(Graph.Authentication.route) {
                        popUpTo(Graph.Main.route) {
                            inclusive = true
                        }
                    }
                },
                onDetailClick = { id->
                    navController.navigate("Detail/$id")
                },
                onNavigateToStatus = { transaction ->
                    navController.navigate("Status/$transaction")
                }
            )
        }
    }
}

@Composable
fun BottomBar(navController: NavHostController,badgeFavorite: Int) {
    val screens = listOf(
        Bottom.Home, Bottom.Store, Bottom.Wishlist, Bottom.Transaction
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomBarDestination = screens.any {
        it.route == currentDestination?.route }
    if (bottomBarDestination) {
        NavigationBar {
            screens.forEach { screen ->
                AddItem(
                    screen = screen,
                    currentDestination = currentDestination,
                    navController = navController,
                    badgeFavorite = badgeFavorite
                )
            }
        }
    }
}

@Composable
fun RowScope.AddItem(
    screen: Bottom,
    currentDestination: NavDestination?,
    navController: NavHostController,
    badgeFavorite : Int
) {
    NavigationBarItem(
        label = {
            Text(text = screen.title, style = MaterialTheme.typography.labelMedium)
        },
        colors = NavigationBarItemDefaults.colors(indicatorColor = PurplePink),
        icon = {
            if(screen.icon == Icons.Default.Favorite){
                if(badgeFavorite>0){
                BadgedBox(
                    badge = {
                        Badge {
                            Text(
                                text = badgeFavorite.toString(),
                                color = Color.White
                            )
                        }
                    }) {
                    Icon(
                        Icons.Default.Favorite,
                        contentDescription = "Favorite"
                    )
                }}else{
                    Icon(
                        Icons.Default.Favorite,
                        contentDescription = "Favorite"
                    )
                }
            }else {
                Icon(
                    imageVector = screen.icon,
                    contentDescription = "Navigation Icon"
                )
            }
        },
        selected = currentDestination?.hierarchy?.any {
            it.route == screen.route
        } == true,
        onClick = {

            navController.navigate(screen.route) {
                popUpTo(navController.graph.findStartDestination().id) {
                    inclusive = true
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        },
    )
}

@ExperimentalMaterial3Api
@Composable
@Preview(showBackground = true)
fun MainPreview(){
    MainScreen(navController = rememberNavController())
}