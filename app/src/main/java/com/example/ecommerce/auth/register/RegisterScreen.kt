package com.example.ecommerce.auth.register

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ecommerce.R
import com.example.ecommerce.api.request.AuthRequest
import com.example.ecommerce.api.response.BaseResponse
import com.example.ecommerce.auth.login.DividerButton
import com.example.ecommerce.auth.login.EmailComponent
import com.example.ecommerce.auth.login.PasswordComponent
import com.example.ecommerce.auth.login.TextTermCondition
import com.example.ecommerce.component.ProgressDialog
import com.example.ecommerce.component.ToastMessage
import com.example.ecommerce.ui.theme.Purple
import com.example.ecommerce.ui.theme.textColor
import com.example.ecommerce.util.Constant

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
    val registerViewModel : RegisterViewModel = hiltViewModel()
    registerViewModel.registerResult.observe(lifecycleOwner) {
        when (it) {
            is BaseResponse.Loading -> {
                isDialog = true
            }

            is BaseResponse.Success -> {
                isDialog = false
                showMessage.showMsg(context,it.data!!.message)
                onRegisterSubmitted()
                registerViewModel.saveAccessToken(it.data.data.accessToken)
            }

            is BaseResponse.Error -> {
                isDialog = false
                showMessage.showMsg(context,it.msg.toString())
            }
        }
    }

    Scaffold(
        topBar = {
            Column {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            stringResource(id = R.string.register),
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                )
                Divider()
            }
        }
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(it)
            .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            EmailComponent(input = email,
                inputError = emailError)

            Spacer(modifier = Modifier.height(16.dp))

            PasswordComponent(
                password = password,
                passwordError = passwordError)

            Button(
                onClick = {
                   registerViewModel.registerUser(Constant.API_KEY, AuthRequest(email.value, password.value,Constant.FIREBASE_TOKEN))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                colors = ButtonDefaults.buttonColors(Purple),
                enabled = !emailError.value && !passwordError.value && email.value.isNotEmpty() && password.value.isNotEmpty()
            ) {
                Text(
                    text = stringResource(id = R.string.register),
                    fontWeight = FontWeight.W500
                )
            }

            DividerButton(false)

            OutlinedButton(
                onClick = { onLoginClick() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                enabled = true
            ) {
                Text(
                    color = Purple,
                    text = stringResource(id = R.string.login),
                    fontWeight = FontWeight.W500
                )
            }

            TextTermCondition(false)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(showBackground = true)
fun RegisterPreview(){
    RegisterScreen(onRegisterSubmitted = { /*TODO*/ }) {

    }
}