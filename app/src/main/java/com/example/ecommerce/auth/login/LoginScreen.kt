package com.example.ecommerce.auth.login

import android.util.Log
import android.util.Patterns
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ecommerce.R
import com.example.ecommerce.api.request.AuthRequest
import com.example.ecommerce.api.response.BaseResponse
import com.example.ecommerce.component.ProgressDialog
import com.example.ecommerce.component.ToastMessage
import com.example.ecommerce.graph.Graph
import com.example.ecommerce.main.main.MainViewModel
import com.example.ecommerce.ui.theme.Purple
import com.example.ecommerce.ui.theme.textColor
import com.example.ecommerce.util.Constant
import kotlinx.coroutines.flow.collect

@OptIn(ExperimentalComposeUiApi::class)
@ExperimentalMaterial3Api
@Composable
fun LoginScreen(
    onNavigateToHome: () -> Unit,
    onLoginSubmitted: () -> Unit,
    onRegisterClick: () -> Unit,
    onNavigateToBoarding: () -> Unit,
) {
    val loginViewModel: LoginViewModel = hiltViewModel()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val keyboardController = LocalSoftwareKeyboardController.current

    var isLoading by remember { mutableStateOf(false) }

    val progressDialog = ProgressDialog()
    if (isLoading) progressDialog.ProgressDialog()

    val email = rememberSaveable { mutableStateOf("") }
    val emailError = rememberSaveable { mutableStateOf(false) }

    val password = rememberSaveable { mutableStateOf("") }
    val passwordError = rememberSaveable { mutableStateOf(false) }

    val fcmToken = loginViewModel.fcmToken.collectAsStateWithLifecycle().value
    Log.d("fcmToken",fcmToken.toString())
    loginViewModel.loginResult.observe(lifecycleOwner){
        when (it) {
            is BaseResponse.Loading -> {
                isLoading = true
            }

            is BaseResponse.Success -> {
                isLoading = false
                loginViewModel.saveAccessToken(it.data!!.data.accessToken)
                loginViewModel.saveLoginState(true)
                loginViewModel.subscribeFcmTopic()
                Log.d("SubscribeFcmTopic",loginViewModel.subscribeFcmTopic().toString())
                onNavigateToHome()
            }

            is BaseResponse.Error -> {
                isLoading = false
                ToastMessage().showMsg(context,it.msg.toString())
            }

            else -> {}
        }
    }

    Scaffold(
        topBar = {
            Column {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            stringResource(id = R.string.login),
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
                        loginViewModel.loginUser(
                            Constant.API_KEY,
                            AuthRequest(email.value, password.value, fcmToken!!)
                        )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                colors = ButtonDefaults.buttonColors(Purple),
                enabled =  !emailError.value && !passwordError.value && email.value.isNotEmpty() && password.value.isNotEmpty()
            ) {
                Text(
                    text = stringResource(id = R.string.login),
                    fontWeight = FontWeight.W500
                )
            }

            DividerButton(true)

            OutlinedButton(
                onClick = {
                    keyboardController?.hide()
                    onRegisterClick()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Text(
                    color = Purple,
                    text = stringResource(id = R.string.register),
                    style = MaterialTheme.typography.labelLarge
                )
            }

            TextTermCondition(true)
        }
    }
}

@Composable
fun DividerButton(tryingToLogin: Boolean = true){
    Row(verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Row (modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.Start){
            Divider()
        }

        Text(
            modifier = Modifier.padding(horizontal = 10.dp),
            text = if(tryingToLogin)stringResource(id = R.string.daftar_dengan) else stringResource(id = R.string.masuk_dengan),
            fontSize = MaterialTheme.typography.bodySmall.fontSize,
            fontWeight = FontWeight.Normal
        )

        Row (modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.End){
            Divider()
        }
    }
}

@Composable
fun TextTermCondition(tryingToLogin: Boolean = true) {
    val initialText = if (tryingToLogin) stringResource(id = R.string.by_entering_login)+" " else stringResource(id = R.string.by_entering_register)+" "
    val syarat = stringResource(id = R.string.term_condition) +" "
    val serta = stringResource(id = R.string.and) +" "
    val kebijakan = stringResource(id = R.string.privacy_policy) +" "
    val toko = stringResource(id = R.string.toko_phincon)

    val annotatedString = buildAnnotatedString {
        append(initialText)
        withStyle(style = SpanStyle(color = Purple)) {
            pushStringAnnotation(tag = syarat, annotation = syarat)
            append(syarat)
        }
        append(serta)
        withStyle(style = SpanStyle(color = Purple)){
            pushStringAnnotation(tag = syarat, annotation = syarat)
            append(kebijakan)
        }
        append(toko)
    }

    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
        style = TextStyle(
            textAlign = TextAlign.Center,
            fontSize = MaterialTheme.typography.bodySmall.fontSize,
            fontWeight = FontWeight.Normal
        ),
        text = annotatedString
    )
}

@Composable
fun EmailComponent(input : MutableState<String>,
                   inputError : MutableState<Boolean>,
) {
    //val localFocusManager = LocalFocusManager.current
    OutlinedTextField(modifier = Modifier
        .fillMaxWidth(),
        label = {
            Text(
                text = stringResource(id = R.string.email),
                style = MaterialTheme.typography.bodyMedium,
            )
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next),
        singleLine = true,
        maxLines = 1,
        value = input.value,
        onValueChange = {
            input.value = it
            inputError.value = !Patterns.EMAIL_ADDRESS.matcher(it).matches() && it.isNotEmpty()
        },
        isError = inputError.value
    )

    if(inputError.value){
        TextFieldError(textError = R.string.email_invalid,
            color = MaterialTheme.colorScheme.error)
    }else {
        TextFieldErrorEmail(textError = if(input.value.isEmpty()) "Contoh: test@gmail.com" else "Contoh: ${input.value}",
            color = Color.Gray
        )
    }
}

@Composable
fun PasswordComponent(password : MutableState<String>,
                      passwordError : MutableState<Boolean>
) {
    val localFocusManager = LocalFocusManager.current
    val passwordVisible = remember { mutableStateOf(false) }

    OutlinedTextField(modifier = Modifier
        .fillMaxWidth(),
        label = {
            Text(
                text = stringResource(id = R.string.password),
                style = MaterialTheme.typography.bodyMedium,
            )
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        singleLine = true,
        keyboardActions = KeyboardActions {
            localFocusManager.clearFocus()
        },
        maxLines = 1,
        value = password.value,
        onValueChange = {
            password.value = it
            passwordError.value = it.isNotEmpty() && it.length <=7
        },
        trailingIcon = {
            val iconImage = if (passwordVisible.value) {
                Icons.Default.Visibility
            } else {
                Icons.Default.VisibilityOff
            }

            val description = if (passwordVisible.value) {
                stringResource(id = R.string.hide_password)
            } else {
                stringResource(id = R.string.show_password)
            }

            IconButton(onClick = {
                passwordVisible.value = !passwordVisible.value
            }) {
                Icon(
                    imageVector = iconImage,
                    contentDescription = description
                )
            }

        },
        visualTransformation = if (passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
        isError = passwordError.value
    )

    if(passwordError.value){
        TextFieldError(textError = R.string.password_invalid, color = MaterialTheme.colorScheme.error)
    }else {
        TextFieldError(textError = R.string.password_min, color = Color.Gray)
    }
}

@Composable
fun TextFieldError(textError: Int, color: Color) {
    Text(
        modifier = Modifier
            .padding(top = 2.dp, start = 16.dp)
            .fillMaxWidth(),
        text = stringResource(id = textError),
        color = color,
        style = MaterialTheme.typography.bodySmall
    )
}

@Composable
fun TextFieldErrorEmail(textError: String, color: Color) {
    Text(
        modifier = Modifier
            .padding(top = 2.dp, start = 16.dp)
            .fillMaxWidth(),
        text = textError,
        color = color,
        style = MaterialTheme.typography.bodySmall
    )
}

@ExperimentalMaterial3Api
@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    Column(modifier = Modifier.fillMaxSize()){
        LoginScreen(onNavigateToHome = {}, onLoginSubmitted = {},
            onRegisterClick = {}, onNavigateToBoarding = {}
        )
    }
}