package com.example.ecommerce.auth.login

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ecommerce.api.request.AuthRequest

@ExperimentalMaterial3Api
@Composable
fun LoginRoute(
    onLoginSubmitted: () -> Unit,
    onRegisterClick: () -> Unit
) {

    val loginViewModel : LoginViewModel = viewModel()
    val api = "6f8856ed-9189-488f-9011-0ff4b6c08edc"
    LoginScreen(
        onLoginSubmitted =  {
            loginViewModel.loginUser(api, AuthRequest("test@gmail.com","12345678","asdf-qwer-zxcv"))
            //onLoginSubmitted()
        },
        onRegisterClick = onRegisterClick)
}