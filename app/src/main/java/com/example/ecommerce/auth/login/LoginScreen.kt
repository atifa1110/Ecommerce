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
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
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
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
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
import androidx.lifecycle.Observer
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ecommerce.util.CommonDialog
import com.example.ecommerce.R
import com.example.ecommerce.api.response.BaseResponse
import com.example.ecommerce.util.showMsg
import com.example.ecommerce.ui.theme.Purple
import com.example.ecommerce.ui.theme.textColor

@ExperimentalMaterial3Api
@Composable
fun LoginScreen(
    onLoginSubmitted: () -> Unit,
    onRegisterClick: () -> Unit
) {

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var isDialog by remember { mutableStateOf(false) }

    if (isDialog)
        CommonDialog()

    val loginViewModel : LoginViewModel = hiltViewModel()

    loginViewModel.loginResult.observe(lifecycleOwner, Observer {
        when (it) {
            is BaseResponse.Loading -> {
                isDialog = true
            }

            is BaseResponse.Success -> {
                isDialog = false
                Log.d("LoginResponse",it.toString())
                context.showMsg(it.toString())
            }

            is BaseResponse.Error -> {
                isDialog = false
                Log.d("LoginResponse",it.msg.toString())
                context.showMsg(it.msg.toString())
            }
            else -> {
                context.showMsg("Data is Empty")
            }
        }
    })
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(modifier = Modifier
                .drawBehind {
                    val borderSize = 1.dp.toPx()
                    drawLine(
                        color = Color.LightGray,
                        start = Offset(0f,size.height),
                        end = Offset(size.width,size.height),
                        strokeWidth = borderSize
                    )
            },
                title = {
                    Text(stringResource(id = R.string.login),
                    fontSize = 22.sp, color = textColor,
                    fontWeight = FontWeight.Normal)
                }
            )
        }
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(it)
            .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            var email = rememberSaveable { mutableStateOf("") }
            var emailError = rememberSaveable { mutableStateOf(false) }

            var password = rememberSaveable { mutableStateOf("") }
            var passwordError = rememberSaveable { mutableStateOf(false) }

            Spacer(modifier = Modifier.height(20.dp))

            EmailComponent(input = email,
                inputError = emailError)

            Spacer(modifier = Modifier.height(16.dp))

            PasswordComponent(
                password = password,
                passwordError = passwordError)

            Button(
                onClick = { onLoginSubmitted() },
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
                onClick = { onRegisterClick() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                enabled = true
            ) {
                Text(
                    color = Purple,
                    text = stringResource(id = R.string.register),
                    fontWeight = FontWeight.W500
                )
            }

            TextSyaratKetentuan(true)
        }
    }
}

@Composable
fun DividerButton(tryingToLogin: Boolean = true){
    Row(
        modifier = Modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier
            .height(1.dp)
            .width(120.dp)
            .background(Color.LightGray))
        Text(modifier = Modifier.padding(horizontal = 20.dp),
            text = if(tryingToLogin)stringResource(id = R.string.daftar_dengan) else stringResource(id = R.string.masuk_dengan),
            fontSize = MaterialTheme.typography.bodySmall.fontSize,
            fontWeight = FontWeight.Normal
        )
        Spacer(modifier = Modifier
            .height(1.dp)
            .width(120.dp)
            .background(Color.LightGray))
    }
}

@Composable
fun TextSyaratKetentuan(tryingToLogin: Boolean = true) {
    val initialText = if (tryingToLogin) "Dengan masuk disini, kamu menyetujui " else "Dengan daftar disini, kamu menyetujui "
    val syarat = "Syarat & Ketentuan"
    val serta = "serta "
    val kebijakan = "Kebijakan Privasi "
    val toko = "TokoPhincon."

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
        colors =
        if (!inputError.value) TextFieldDefaults.outlinedTextFieldColors(
            backgroundColor = Color.Transparent,
            focusedLabelColor = Gray
        )else TextFieldDefaults.outlinedTextFieldColors(
            backgroundColor = Color.Transparent,
            cursorColor = MaterialTheme.colorScheme.error,
            focusedBorderColor = MaterialTheme.colorScheme.error,
            focusedLabelColor = MaterialTheme.colorScheme.error,
        ),

        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next),
        singleLine = true,
        maxLines = 1,
        value = input.value,
        onValueChange = {
            input.value = it
            inputError.value = !Patterns.EMAIL_ADDRESS.matcher(it).matches() || it.isEmpty()
        },
        isError = inputError.value
    )

    if(inputError.value){
        TextFieldError(textError = R.string.email_invalid,
            color = MaterialTheme.colorScheme.error)
    }else {
        if(input.value.isNotEmpty()) {
            TextFieldErrorEmail(textError = "Contoh: ${input.value}",
                color = Gray)
        }
    }
}

@Composable
fun PasswordComponent(password : MutableState<String>,
                               passwordError : MutableState<Boolean>) {

    val localFocusManager = LocalFocusManager.current
    val passwordVisible = remember { mutableStateOf(false) }

    OutlinedTextField(modifier = Modifier
        .fillMaxWidth(),
        label = {
            Text(
                text = stringResource(id = R.string.password),
                style = MaterialTheme.typography.bodyMedium,
            )
        },colors =
        if (!passwordError.value) TextFieldDefaults.outlinedTextFieldColors(
            backgroundColor = Color.Transparent,
            focusedLabelColor = Gray
        )else TextFieldDefaults.outlinedTextFieldColors(
            backgroundColor = Color.Transparent,
            cursorColor = MaterialTheme.colorScheme.error,
            focusedBorderColor =MaterialTheme.colorScheme.error,
            focusedLabelColor = MaterialTheme.colorScheme.error
            //unfocusedBorderColor = Color.Red
        ),
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
            passwordError.value = password.value.isEmpty() || password.value.length <=7
            //onTextSelected(it)
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
        if(password.value.isNotEmpty()) {
            TextFieldError(textError = R.string.password_min, color = Color.Gray)
        }
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
    Box(modifier = Modifier.fillMaxSize()){
        LoginScreen(onLoginSubmitted = {}) {
            
        }
    }
}