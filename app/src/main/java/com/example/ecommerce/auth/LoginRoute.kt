package com.example.ecommerce.auth

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable

@ExperimentalMaterial3Api
@Composable
fun LoginRoute(
    onLoginSubmitted: () -> Unit,
    onRegisterClick: () -> Unit
) {
    LoginScreen(
        onLoginSubmitted =  onLoginSubmitted,onRegisterClick = onRegisterClick)
}