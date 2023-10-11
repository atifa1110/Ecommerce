package com.example.ecommerce.main.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Badge
import androidx.compose.material.BadgedBox
import androidx.compose.material.IconButton
import androidx.compose.material.NavigationRail
import androidx.compose.material.NavigationRailItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Reorder
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ecommerce.graph.Bottom
import com.example.ecommerce.graph.HomeNavigation
import com.example.ecommerce.ui.theme.PurplePink

@ExperimentalMaterial3Api
@Composable
fun MainScreen(
    onLogoutClick: () -> Unit,
    onDetailClick: (id: String) -> Unit,
    onNavigateToStatus: (fulfillment: String) -> Unit,
    onNavigateToCart: () -> Unit,
    onNavigateToNotification: () -> Unit,
    onNavigateToModular: () -> Unit,
    windowSizeClass: WindowSizeClass
) {
    val navBarController = rememberNavController()
    val mainViewModel = hiltViewModel<MainViewModel>()

    val cartSize = mainViewModel.cartSize.collectAsStateWithLifecycle(emptyList()).value.size
    val badgeCart = if (cartSize == 0) 0 else cartSize

    val favoriteSize =
        mainViewModel.favoriteSize.collectAsStateWithLifecycle(emptyList()).value.size
    val badgeFavorite = if (favoriteSize == 0) 0 else favoriteSize

    val notificationSize =
        mainViewModel.notificationSize.collectAsStateWithLifecycle(emptyList()).value.size
    val badgeNotification = if (notificationSize == 0) 0 else notificationSize

    val profile = mainViewModel.getProfileName()

    Scaffold(
        modifier = Modifier.navigationBarsPadding(),
        topBar = {
            Column(modifier = Modifier) {
                TopAppBar(
                    title = {
                        Text(
                            text = profile,
                            style = MaterialTheme.typography.titleLarge
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {}) {
                            Icon(Icons.Default.AccountCircle, "account_profile")
                        }
                    },
                    actions = {
                        IconButton(onClick = { onNavigateToNotification() }) {
                            if (notificationSize > 0) {
                                BadgedBox(
                                    badge = {
                                        Badge {
                                            Text(
                                                text = badgeNotification.toString(),
                                                color = Color.White
                                            )
                                        }
                                    }
                                ) {
                                    Icon(
                                        Icons.Filled.Notifications,
                                        contentDescription = "Notifications"
                                    )
                                }
                            } else {
                                Icon(Icons.Filled.Notifications, contentDescription = null)
                            }
                        }
                        IconButton(onClick = { onNavigateToCart() }) {
                            if (cartSize > 0) {
                                BadgedBox(
                                    badge = {
                                        Badge {
                                            Text(
                                                text = badgeCart.toString(),
                                                color = Color.White
                                            )
                                        }
                                    }
                                ) {
                                    Icon(
                                        Icons.Filled.ShoppingCart,
                                        contentDescription = "Cart"
                                    )
                                }
                            } else {
                                Icon(Icons.Filled.ShoppingCart, contentDescription = "Cart")
                            }
                        }
                        IconButton(onClick = {
                            onNavigateToModular()
                        }) {
                            Icon(Icons.Filled.Reorder, contentDescription = null)
                        }
                    }
                )
                Divider()
            }
        },
        bottomBar = {
            if (windowSizeClass.heightSizeClass != WindowHeightSizeClass.Compact) {
                BottomBar(navBarController, badgeFavorite)
            } else {
                val screens = listOf(
                    Bottom.Home,
                    Bottom.Store,
                    Bottom.Wishlist,
                    Bottom.Transaction
                )
                var selectedItemIndex by rememberSaveable { mutableStateOf(0) }
                NavigationSideBar(
                    items = screens,
                    selectedItemIndex = selectedItemIndex,
                    onNavigate = { selectedItemIndex = it }
                )
            }
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            HomeNavigation(
                navController = navBarController,
                onLogoutClick = onLogoutClick,
                onDetailClick = onDetailClick,
                onNavigateToStatus = onNavigateToStatus
            )
        }
    }
}

@Composable
fun NavigationSideBar(
    items: List<Bottom>,
    selectedItemIndex: Int,
    onNavigate: (Int) -> Unit
) {
    NavigationRail(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.inverseOnSurface)
            .offset(x = (-1).dp)
    ) {
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.Top)
        ) {
            items.forEachIndexed { index, item ->
                NavigationRailItem(
                    selected = selectedItemIndex == index,
                    onClick = {
                        onNavigate(index)
                    },
                    icon = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = "Navigation Icon"
                        )
                    },
                    label = {
                        Text(text = item.title)
                    },
                )
            }
        }
    }
}

@Composable
fun BottomBar(navController: NavHostController, badgeFavorite: Int) {
    val screens = listOf(
        Bottom.Home,
        Bottom.Store,
        Bottom.Wishlist,
        Bottom.Transaction
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomBarDestination = screens.any {
        it.route == currentDestination?.route
    }
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
    badgeFavorite: Int
) {
    NavigationBarItem(
        label = {
            Text(text = screen.title, style = MaterialTheme.typography.labelMedium)
        },
        colors = NavigationBarItemDefaults.colors(indicatorColor = PurplePink),
        icon = {
            if (screen.icon == Icons.Default.Favorite) {
                if (badgeFavorite > 0) {
                    BadgedBox(
                        badge = {
                            Badge {
                                Text(
                                    text = badgeFavorite.toString(),
                                    color = Color.White
                                )
                            }
                        }
                    ) {
                        Icon(
                            Icons.Default.Favorite,
                            contentDescription = "Favorite"
                        )
                    }
                } else {
                    Icon(
                        Icons.Default.Favorite,
                        contentDescription = "Favorite"
                    )
                }
            } else {
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
fun MainPreview() {
//    MainScreen(
//        onLogoutClick = { },
//        onDetailClick = {},
//        onNavigateToStatus = {},
//        onNavigateToCart = {},
//        onNavigateToNotification = {},
//        onNavigateToModular = {},
//
//    )
}
