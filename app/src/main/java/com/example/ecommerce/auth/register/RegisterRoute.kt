package com.example.ecommerce.auth.register

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable

@ExperimentalMaterial3Api
@Composable
fun RegisterRoute(
    onRegisterSubmitted: () -> Unit,
    onLoginClick: () -> Unit
) {
    RegisterScreen(
        onRegisterSubmitted =  onRegisterSubmitted,onLoginClick = onLoginClick)
}