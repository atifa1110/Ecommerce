package com.example.ecommerce.auth.register

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable

@ExperimentalMaterial3Api
@Composable
fun RegisterRoute(
    onNavigateToProfile: () -> Unit,
    onNavigateToLogin: () -> Unit
) {

    RegisterScreen(
        onRegisterSubmitted = onNavigateToProfile,
        onLoginClick = onNavigateToLogin
    )
}