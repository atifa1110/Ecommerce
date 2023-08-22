package com.example.ecommerce.auth.profile

import androidx.compose.runtime.Composable

@Composable
fun ProfileRoute (
    email:String?,
    password:String?,
    onNavigateToHome : () -> Unit
){

    ProfileScreen(email,password,onNavigateToHome)
}