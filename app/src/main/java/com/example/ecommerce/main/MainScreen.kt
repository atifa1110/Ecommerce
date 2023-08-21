package com.example.ecommerce.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Reorder
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ecommerce.R
import com.example.ecommerce.graph.Bottom
import com.example.ecommerce.graph.Graph
import com.example.ecommerce.graph.HomeNavigation
import com.example.ecommerce.ui.theme.LightGray
import com.example.ecommerce.ui.theme.textColor

@ExperimentalMaterial3Api
@Composable
fun MainScreen(navController: NavHostController) {
    val navBarController = rememberNavController()

    Scaffold(
        topBar = {
            TopAppBar(modifier = Modifier
                .drawBehind {
                    val borderSize = 1.dp.toPx()
                    drawLine(
                        color = LightGray,
                        start = Offset(0f,size.height),
                        end = Offset(size.width,size.height),
                        strokeWidth = borderSize
                    )
                },
                title = {
                    Text(
                        stringResource(id = R.string.example_name),
                        fontSize = 22.sp, color = textColor,
                        fontWeight = FontWeight.Normal)
                },
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.AccountCircle,"account_profile")
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Filled.Notifications, contentDescription = null)
                    }
                    IconButton(onClick = {}) {
                        Icon(Icons.Filled.ShoppingCart, contentDescription = null)
                    }
                    IconButton(onClick = {}) {
                        Icon(Icons.Filled.Reorder, contentDescription = null)
                    }
                }
            )
        },
        bottomBar = {
            BottomBar(navController = navBarController)
        }
    ) {
        Box(modifier = Modifier.padding(it)) {
            HomeNavigation(navController = navBarController){
                navController.navigate(Graph.Authentication.route) {
                    popUpTo(Graph.Home.route) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }

            }
        }
    }
}

@Composable
fun BottomBar(navController: NavHostController) {
    val screens = listOf(
        Bottom.Home, Bottom.Store, Bottom.Wishlist, Bottom.Transaction
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomBarDestination = screens.any { it.route == currentDestination?.route }
    if (bottomBarDestination) {
        NavigationBar {
            screens.forEach { screen ->
                AddItem(
                    screen = screen,
                    currentDestination = currentDestination,
                    navController = navController
                )
            }
        }
    }
}

@Composable
fun RowScope.AddItem(
    screen: Bottom,
    currentDestination: NavDestination?,
    navController: NavHostController
) {
    NavigationBarItem(
        label = {
            Text(text = screen.title)
        },
        icon = {
            Icon(
                imageVector = screen.icon,
                contentDescription = "Navigation Icon"
            )
        },
        selected = currentDestination?.hierarchy?.any {
            it.route == screen.route
        } == true,
        onClick = {
            navController.navigate(screen.route) {
                popUpTo(navController.graph.findStartDestination().id)
                launchSingleTop = true
            }
        }
    )
}

@ExperimentalMaterial3Api
@Composable
@Preview(showBackground = true)
fun MainPreview(){
    //MainScreen()
}