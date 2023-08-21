package com.example.ecommerce.util

import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Dialog

@Composable
fun CommonDialog(){
    Dialog(onDismissRequest = {}) {
        CircularProgressIndicator()
    }
}
