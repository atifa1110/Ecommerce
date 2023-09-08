package com.example.ecommerce.component

import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Dialog

class ProgressDialog() {
    @Composable
    fun ProgressDialog() {
        Dialog(onDismissRequest = {}) {
            CircularProgressIndicator()
        }
    }
}

