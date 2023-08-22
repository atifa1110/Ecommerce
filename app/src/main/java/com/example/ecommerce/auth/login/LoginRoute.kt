package com.example.ecommerce.auth.login

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ecommerce.util.Constant
import com.example.ecommerce.api.request.AuthRequest

@ExperimentalMaterial3Api
@Composable
fun LoginRoute(
    onNavigateToHome: () -> Unit,
    onNavigateToRegister: () -> Unit
) {

    LoginScreen(
        onNavigateToHome = onNavigateToHome,
        onLoginSubmitted =  {},
        onRegisterClick = onNavigateToRegister
    )
}