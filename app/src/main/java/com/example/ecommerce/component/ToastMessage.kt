package com.example.ecommerce.component

import android.content.Context
import android.widget.Toast

class ToastMessage() {
    fun showMsg(
        context: Context,
        msg: String,
        duration: Int = Toast.LENGTH_SHORT
    ) = Toast.makeText(context, msg, duration).show()
}
