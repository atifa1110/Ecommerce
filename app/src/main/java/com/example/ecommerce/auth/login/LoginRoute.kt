package com.example.ecommerce.auth.login

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable

@ExperimentalMaterial3Api
@Composable
fun LoginRoute(
    onNavigateToHome: () -> Unit,
    onNavigateToBoarding: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    LoginScreen(
        onNavigateToHome = onNavigateToHome,
        onNavigateToBoarding = onNavigateToBoarding,
        onRegisterClick = onNavigateToRegister
    )
}
