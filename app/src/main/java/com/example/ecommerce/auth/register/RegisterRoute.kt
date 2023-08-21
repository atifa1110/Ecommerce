package com.example.ecommerce.auth.register

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ecommerce.api.request.AuthRequest
import com.example.ecommerce.auth.login.LoginViewModel
import com.example.ecommerce.util.Constant

@ExperimentalMaterial3Api
@Composable
fun RegisterRoute(
    onNavigateToProfile: () -> Unit,
    onNavigateToLogin: () -> Unit
) {

    val registerViewModel : RegisterViewModel = viewModel()
    val api = Constant.API_KEY
    RegisterScreen(
        onRegisterSubmitted = {
            registerViewModel.registerUser(api, AuthRequest("test@gmail.com","12345678","asdf-qwer-zxcv"))
            onNavigateToProfile()
        },
        onLoginClick = onNavigateToLogin)
}