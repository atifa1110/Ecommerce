package com.example.ecommerce.auth

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecommerce.R
import com.example.ecommerce.ui.theme.EnableButton
import com.example.ecommerce.ui.theme.textColor

@ExperimentalMaterial3Api
@Composable
fun RegisterScreen(
    onRegisterSubmitted: () -> Unit,
    onLoginClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                ) {
                    Text("Daftar", fontSize = 22.sp, color = textColor, fontWeight = FontWeight.Normal)

                    Spacer(modifier = Modifier.height(1.dp).fillMaxWidth().background(Color.LightGray))
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
                onClick = { onRegisterSubmitted() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                colors = ButtonDefaults.buttonColors(EnableButton),
                enabled =  emailState.isValid && passwordState.isValid
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
                    color = EnableButton,
                    text = stringResource(id = R.string.login),
                    fontWeight = FontWeight.W500
                )
            }

            TextSyaratKetentuan(false)
        }
    }
}