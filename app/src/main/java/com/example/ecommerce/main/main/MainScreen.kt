package com.example.ecommerce.main.main

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Reorder
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Devices
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
import com.example.ecommerce.NavigationType
import com.example.ecommerce.graph.Bottom
import com.example.ecommerce.graph.HomeNavigation
import com.example.ecommerce.ui.theme.EcommerceTheme
import com.example.ecommerce.ui.theme.PurplePink
import com.example.ecommerce.ui.theme.errorDark
import com.example.ecommerce.ui.theme.primaryContainerDark

@ExperimentalMaterial3Api
@Composable
fun MainScreen(
    onLogoutClick: () -> Unit,
    onDetailClick: (id: String) -> Unit,
    onNavigateToStatus: (fulfillment: String) -> Unit,
    onNavigateToCart: () -> Unit,
    onNavigateToNotification: () -> Unit,
    onNavigateToModular: () -> Unit,
    navigationType: NavigationType
) {
    val navBarController = rememberNavController()
    val mainViewModel = hiltViewModel<MainViewModel>()

    val cartSize = mainViewModel.cartSize.collectAsStateWithLifecycle(emptyList()).value.size
    val badgeCart = if (cartSize == 0) 0 else cartSize

    val favoriteSize = mainViewModel.favoriteSize.collectAsStateWithLifecycle(emptyList()).value.size
    val badgeFavorite = if (favoriteSize == 0) 0 else favoriteSize

    val notificationSize = mainViewModel.notificationSize.collectAsStateWithLifecycle(emptyList()).value.size
    val badgeNotification = if (notificationSize == 0) 0 else notificationSize

    val profile = mainViewModel.getProfileName()

    val screens = listOf(Bottom.Home, Bottom.Store, Bottom.Wishlist, Bottom.Transaction)

    val navBackStackEntry by navBarController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val selectedItemIndex by rememberSaveable { mutableStateOf(0) }

    val navigationRail = navigationType == NavigationType.NAV_RAIL

    MainContent(
        navigationRail = navigationRail,
        items = screens,
        currentDestination = currentDestination,
        navBarController = navBarController,
        badgeFavorite = badgeFavorite,
        selectedItemIndex = selectedItemIndex,
        profile = profile,
        onLogoutClick = { onLogoutClick() },
        onDetailClick = { id -> onDetailClick(id) },
        onNavigateToStatus = { fulfillment ->  onNavigateToStatus(fulfillment)},
        onNavigateToCart = { onNavigateToCart() },
        onNavigateToNotification = { onNavigateToNotification() },
        onNavigateToModular = { onNavigateToModular() },
        notificationSize = notificationSize,
        badgeNotification = badgeNotification,
        cartSize = cartSize,
        badgeCart = badgeCart
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContent(
    navigationRail : Boolean,
    items: List<Bottom>,
    currentDestination: NavDestination?,
    navBarController: NavHostController,
    badgeFavorite : Int,
    selectedItemIndex: Int,
    profile : String,
    onLogoutClick: () -> Unit,
    onDetailClick: (id: String) -> Unit,
    onNavigateToStatus: (fulfillment: String) -> Unit,
    onNavigateToCart: () -> Unit,
    onNavigateToNotification: () -> Unit,
    onNavigateToModular: () -> Unit,
    notificationSize : Int,
    badgeNotification : Int,
    cartSize : Int,
    badgeCart : Int
){
    Row (modifier = Modifier.fillMaxSize()) {
        if(navigationRail){
            NavigationSideBar(
                items = items,
                currentDestination = currentDestination,
                navController = navBarController,
                badgeFavorite = badgeFavorite,
                selectedItemIndex = selectedItemIndex
            )
        }

        Scaffold(
            modifier = Modifier.navigationBarsPadding(),
            topBar = {
                Column {
                    TopAppBar(
                        title = {
                            Text(
                                text = profile,
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.surface
                            )
                        },
                        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.background),
                        navigationIcon = {
                            IconButton(onClick = {}) {
                                Icon(Icons.Default.AccountCircle, "account_profile")
                            }
                        },
                        actions = {
                            IconButton(
                                modifier = Modifier.width(50.dp),
                                onClick = { onNavigateToNotification() }
                            ) {
                                BadgedBox(
                                    badge = {
                                        if (notificationSize > 0) {
                                            Badge(
                                                containerColor = errorDark
                                            ) {
                                                Text(
                                                    text = badgeNotification.toString(),
                                                    color = Color.White
                                                )
                                            }
                                        }
                                    }
                                ) {
                                    Icon(
                                        Icons.Filled.Notifications,
                                        contentDescription = "Notifications"
                                    )
                                }
                            }

                            IconButton(
                                modifier = Modifier.width(50.dp),
                                onClick = { onNavigateToCart() }
                            ) {
                                BadgedBox(
                                    badge = {
                                        if (cartSize > 0) {
                                            Badge(
                                                containerColor = errorDark
                                            ) {
                                                Text(
                                                    text = badgeCart.toString(),
                                                    color = Color.White
                                                )
                                            }
                                        }
                                    }
                                ) {
                                    Icon(
                                        Icons.Filled.ShoppingCart,
                                        contentDescription = "Cart"
                                    )
                                }
                            }

                            IconButton(onClick = { onNavigateToModular() }) {
                                Icon(Icons.Filled.Reorder, contentDescription = null)
                            }
                        }
                    )
                    Divider()
                }
            },
            bottomBar = {
                if(!navigationRail) {
                    NavigationBottomBar(
                        items = items,
                        currentDestination = currentDestination,
                        navController = navBarController,
                        badgeFavorite = badgeFavorite,
                        selectedItemIndex = selectedItemIndex
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
}

@Composable
fun NavigationSideBar(
    items: List<Bottom>,
    currentDestination: NavDestination?,
    navController: NavHostController,
    badgeFavorite: Int,
    selectedItemIndex: Int
) {
    NavigationRail(
        containerColor = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically)
        ) {
            items.forEachIndexed { index, screen ->
                NavigationRailItem(
                    selected = currentDestination?.hierarchy?.any {
                        it.route == screen.route
                    } == true,
                    //screen.route == "home",
                    colors = NavigationRailItemDefaults.colors(
                        selectedIconColor = primaryContainerDark,
                        indicatorColor = PurplePink
                    ),
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
                    icon = {
                        NavigationIcon(
                            item = screen,
                            selected = selectedItemIndex == index,
                            badgeFavorite = badgeFavorite
                        )
                    },
                )
            }
        }
    }
}

@Composable
fun NavigationBottomBar(
    items: List<Bottom>,
    currentDestination: NavDestination?,
    navController: NavHostController,
    badgeFavorite: Int,
    selectedItemIndex: Int
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.background
    ) {
        items.forEachIndexed { index, screen ->
            NavigationBarItem(
                label = {
                    Text(text = screen.title, style = MaterialTheme.typography.labelMedium)
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = primaryContainerDark,
                    indicatorColor = PurplePink
                ),
                icon = {
                    NavigationIcon(
                        item = screen,
                        selected = selectedItemIndex == index,
                        badgeFavorite = badgeFavorite)
                },
                selected = currentDestination?.hierarchy?.any {
                    it.route == screen.route
                } == true,
                //screen.route == "home",
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
    }

}

@Composable
fun NavigationIcon(
    item: Bottom,
    selected: Boolean,
    badgeFavorite : Int
) {
    BadgedBox(
        badge = {
            if (item.selectedIcon == Icons.Default.Favorite) {
                if(badgeFavorite>0) {
                    Badge(
                        containerColor = errorDark
                    ) {
                        Text(
                            text = badgeFavorite.toString(),
                            color = Color.White
                        )
                    }
                }
            }
        }
    ) {
        Icon(
            imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
            contentDescription = item.title
        )
    }
}

@Composable
@Preview("Light Mode", device = Devices.PIXEL_3)
@Preview("Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
fun MainBottomPreview(){
    val navBarController = rememberNavController()
    val navBackStackEntry by navBarController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val screens = listOf(
        Bottom.Home,
        Bottom.Store,
        Bottom.Wishlist,
        Bottom.Transaction
    )

    EcommerceTheme {
        MainContent(
            navigationRail = false,
            items = screens,
            currentDestination = currentDestination ,
            navBarController = navBarController,
            badgeFavorite = 5,
            selectedItemIndex = 1,
            profile = "Test",
            onLogoutClick = { /*TODO*/ },
            onDetailClick = {},
            onNavigateToStatus = {},
            onNavigateToCart = { /*TODO*/ },
            onNavigateToNotification = { /*TODO*/ },
            onNavigateToModular = { /*TODO*/ },
            notificationSize = 2,
            badgeNotification = 2,
            cartSize = 3,
            badgeCart = 3
        )
    }
}

@Composable
@Preview("Light Mode", device = Devices.TABLET)
@Preview("Dark Mode", device = Devices.TABLET, uiMode = Configuration.UI_MODE_NIGHT_YES)
fun MainRailPreview() {

    val navBarController = rememberNavController()
    val navBackStackEntry by navBarController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val screens = listOf(
        Bottom.Home,
        Bottom.Store,
        Bottom.Wishlist,
        Bottom.Transaction
    )

    EcommerceTheme {
        MainContent(
            navigationRail = true,
            items = screens,
            currentDestination = currentDestination ,
            navBarController = navBarController,
            badgeFavorite = 5,
            selectedItemIndex = 1,
            profile = "Test",
            onLogoutClick = { /*TODO*/ },
            onDetailClick = {},
            onNavigateToStatus = {},
            onNavigateToCart = { /*TODO*/ },
            onNavigateToNotification = { /*TODO*/ },
            onNavigateToModular = { /*TODO*/ },
            notificationSize = 2,
            badgeNotification = 2,
            cartSize = 3,
            badgeCart = 3
        )
    }
}
