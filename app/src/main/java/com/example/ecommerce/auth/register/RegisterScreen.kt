package com.example.ecommerce.auth.register

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecommerce.R
import com.example.ecommerce.auth.login.DividerButton
import com.example.ecommerce.auth.login.EmailComponent
import com.example.ecommerce.auth.login.PasswordComponent
import com.example.ecommerce.auth.login.TextSyaratKetentuan
import com.example.ecommerce.ui.theme.Purple
import com.example.ecommerce.ui.theme.textColor

@ExperimentalMaterial3Api
@Composable
fun RegisterScreen(
    onRegisterSubmitted: (email: String, password: String) -> Unit,
    onLoginClick: () -> Unit
) {
    var email = rememberSaveable { mutableStateOf("") }
    var emailError = rememberSaveable { mutableStateOf(false) }

    var password = rememberSaveable { mutableStateOf("") }
    var passwordError = rememberSaveable { mutableStateOf(false) }

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
                    Text(stringResource(id = R.string.register),
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
            Spacer(modifier = Modifier.height(20.dp))

            EmailComponent(input = email,
                inputError = emailError)

            Spacer(modifier = Modifier.height(16.dp))

            PasswordComponent(
                password = password,
                passwordError = passwordError)

            Button(
                onClick = { onRegisterSubmitted(email.value,password.value) },
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

            TextSyaratKetentuan(false)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(showBackground = true)
fun RegisterPreview(){
//    RegisterScreen(onRegisterSubmitted = {"",""}) {
//
//    }
}