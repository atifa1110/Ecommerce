package com.example.ecommerce.main.home

import androidx.compose.runtime.Composable

@Composable
fun HomeRoute(
    onLogoutClick: () -> Unit
) {
    HomeScreen (
        onLogoutClick
    )
}