package com.example.ecommerce.auth.register

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.api.request.AuthRequest
import com.example.core.api.response.BaseResponse
import com.example.core.util.Constant
import com.example.ecommerce.R
import com.example.ecommerce.auth.login.DividerButton
import com.example.ecommerce.auth.login.EmailComponent
import com.example.ecommerce.auth.login.PasswordComponent
import com.example.ecommerce.auth.login.TextTermCondition
import com.example.ecommerce.auth.profile.ProfileScreen
import com.example.ecommerce.component.ProgressDialog
import com.example.ecommerce.component.ToastMessage
import com.example.ecommerce.ui.theme.EcommerceTheme
import com.example.ecommerce.ui.theme.Purple

@OptIn(ExperimentalComposeUiApi::class)
@ExperimentalMaterial3Api
@Composable
fun RegisterScreen(
    onRegisterSubmitted: () -> Unit,
    onLoginClick: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var isDialog by remember { mutableStateOf(false) }

    if (isDialog) ProgressDialog()

    val email = rememberSaveable { mutableStateOf("") }
    val emailError = rememberSaveable { mutableStateOf(false) }

    val password = rememberSaveable { mutableStateOf("") }
    val passwordError = rememberSaveable { mutableStateOf(false) }

    val showMessage = ToastMessage()
    val registerViewModel: RegisterViewModel = hiltViewModel()
    val fcmToken = registerViewModel.fcmToken.collectAsStateWithLifecycle().value
    Log.d("fcmToken", fcmToken.toString())

    registerViewModel.registerResult.observe(lifecycleOwner) {
        when (it) {
            is BaseResponse.Loading -> {
                isDialog = true
            }

            is BaseResponse.Success -> {
                isDialog = false
                registerViewModel.saveAccessToken(it.data?.data?.accessToken ?: "")
                registerViewModel.saveLoginState(true)
                registerViewModel.registerAnalytics(email.value)
                onRegisterSubmitted()
            }

            is BaseResponse.Error -> {
                isDialog = false
                showMessage.showMsg(context, it.msg.toString())
            }
        }
    }

    RegisterContent(
        email = email,
        emailError = emailError,
        password = password,
        passwordError = passwordError,
        onLoginClick = { onLoginClick() },
        registerUser = {
            registerViewModel.registerUser(
                Constant.API_KEY,
                AuthRequest(email.value, password.value, fcmToken!!)
            )
        },
        buttonAnalytics = { button ->
            registerViewModel.buttonAnalytics(button)
        }
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterContent(
    email : MutableState<String>,
    emailError : MutableState<Boolean>,
    password : MutableState<String>,
    passwordError : MutableState<Boolean>,
    onLoginClick: () -> Unit,
    registerUser: () -> Unit,
    buttonAnalytics: (button: String) -> Unit,
){
    val keyboardController = LocalSoftwareKeyboardController.current
    Scaffold(
        topBar = {
            Column {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            stringResource(id = R.string.register),
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.surface
                        )
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.background)
                )
                Divider()
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            EmailComponent(
                input = email,
                inputError = emailError
            )

            Spacer(modifier = Modifier.height(16.dp))

            PasswordComponent(
                password = password,
                passwordError = passwordError
            )

            Button(
                onClick = {
                    keyboardController?.hide()
                    registerUser()
                    buttonAnalytics("Register")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Purple,
                    contentColor = Color.White
                ),
                enabled = !emailError.value && !passwordError.value && email.value.isNotEmpty() && password.value.isNotEmpty()
            ) {
                Text(
                    text = stringResource(id = R.string.register),
                    fontWeight = FontWeight.W500
                )
            }

            DividerButton(false)

            OutlinedButton(
                onClick = {
                    onLoginClick()
                    buttonAnalytics("Login")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
            ) {
                Text(
                    text = stringResource(id = R.string.login),
                    fontWeight = FontWeight.W500,
                    color = MaterialTheme.colorScheme.primary,
                )
            }

            TextTermCondition(false)
        }
    }
}

@Composable
@Preview("Light Mode", device = Devices.PIXEL_3)
@Preview("Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
fun RegisterPreview() {
    val email = rememberSaveable { mutableStateOf("atifafiorenza24@gmail.com") }
    val emailError = rememberSaveable { mutableStateOf(false) }

    val password = rememberSaveable { mutableStateOf("12345678") }
    val passwordError = rememberSaveable { mutableStateOf(false) }

    EcommerceTheme {
        RegisterContent(
            email = email,
            emailError = emailError,
            password = password,
            passwordError = passwordError,
            onLoginClick = {},
            registerUser = {},
            buttonAnalytics =  {}
        )
    }
}

