package com.example.ecommerce.auth

import android.util.Log
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
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
import com.example.ecommerce.R
import com.example.ecommerce.TextFieldState
import com.example.ecommerce.ui.theme.EnableButton
import com.example.ecommerce.ui.theme.textColor

@ExperimentalMaterial3Api
@Composable
fun LoginScreen(
    onLoginSubmitted: () -> Unit,
    onRegisterClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                ) {
                    Text("Masuk", fontSize = 22.sp, color = textColor, fontWeight = FontWeight.Normal)

                    Spacer(modifier = Modifier
                        .height(1.dp)
                        .fillMaxWidth()
                        .background(Color.LightGray))
                }
            })
        }
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(it)
            .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            val focusRequester = remember { FocusRequester() }
            val emailState = remember { EmailState() }
            val passwordState = remember { PasswordState() }

            val onSubmit = {
                if (emailState.isValid && passwordState.isValid) {
                    //onSignInSubmitted(emailState.text, passwordState.text)
                    Log.d("LoginScreen", "Data is Valid")
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Email(
                emailState = emailState,
                onImeAction = { onSubmit()})

            Spacer(modifier = Modifier.height(16.dp))

            Password(
                label = stringResource(id = R.string.password),
                passwordState = passwordState,
                modifier = Modifier.focusRequester(focusRequester),
                onImeAction = { onSubmit() }
            )

            Button(
                onClick = { onLoginSubmitted() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                colors = ButtonDefaults.buttonColors(EnableButton),
                enabled =  emailState.isValid && passwordState.isValid
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
                    color = EnableButton,
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
    val syarat = "Syarat & Ketentuan "
    val serta = "serta "
    val kebijakan = "Kebijakan Privasi "
    val toko = "TokoPhincon."

    val annotatedString = buildAnnotatedString {
        append(initialText)
        withStyle(style = SpanStyle(color = EnableButton)) {
            pushStringAnnotation(tag = syarat, annotation = syarat)
            append(syarat)
        }
        append(serta)
        withStyle(style = SpanStyle(color = EnableButton)){
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Email(
    emailState: TextFieldState,
    imeAction: ImeAction = ImeAction.Next,
    onImeAction: () -> Unit = {}
) {
    OutlinedTextField(
        value = emailState.text,
        onValueChange = {
            emailState.text = it
        },
        label = {
            Text(
                text = stringResource(id = R.string.email),
                style = MaterialTheme.typography.bodyMedium,
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged { focusState ->
                emailState.onFocusChange(focusState.isFocused)
                if (!focusState.isFocused) {
                    emailState.enableShowErrors()
                }
            },
        textStyle = MaterialTheme.typography.bodyMedium,
        isError = emailState.showErrors(),
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = imeAction,
            keyboardType = KeyboardType.Email
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                onImeAction()
            }
        ),
    )

    emailState.getError()?.let { error -> TextFieldError(textError = error) }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Password(
    label: String,
    passwordState: TextFieldState,
    modifier: Modifier = Modifier,
    imeAction: ImeAction = ImeAction.Done,
    onImeAction: () -> Unit = {}
) {
    val showPassword = rememberSaveable { mutableStateOf(false) }
    OutlinedTextField(
        value = passwordState.text,
        onValueChange = {
            passwordState.text = it
            passwordState.enableShowErrors()
        },
        modifier = modifier
            .fillMaxWidth()
            .onFocusChanged { focusState ->
                passwordState.onFocusChange(focusState.isFocused)
                if (!focusState.isFocused) {
                    passwordState.enableShowErrors()
                }
            },
        textStyle = MaterialTheme.typography.bodyMedium,
        label = {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
            )
        },
        trailingIcon = {
            if (showPassword.value) {
                IconButton(onClick = { showPassword.value = false }) {
                    Icon(
                        imageVector = Icons.Filled.Visibility,
                        contentDescription = stringResource(id = R.string.hide_password)
                    )
                }
            } else {
                IconButton(onClick = { showPassword.value = true }) {
                    Icon(
                        imageVector = Icons.Filled.VisibilityOff,
                        contentDescription = stringResource(id = R.string.show_password)
                    )
                }
            }
        },
        visualTransformation = if (showPassword.value) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        isError = passwordState.showErrors(),
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = imeAction,
            keyboardType = KeyboardType.Password
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                onImeAction()
            }
        ),
    )
    passwordState.getError()?.let { error -> TextFieldError(textError = error) }
}

@Composable
fun TextFieldError(textError: String) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 2.dp)) {
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = textError,
            modifier = Modifier.fillMaxWidth(),
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.error
        )
    }
}

@ExperimentalMaterial3Api
@Preview
@Composable
fun LoginPreview() {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)){
        LoginScreen(onLoginSubmitted = {}) {
            
        }
    }
}