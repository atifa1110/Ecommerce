package com.example.core.api.model

import android.os.Build
import android.os.Bundle
import androidx.navigation.NavType
import com.example.core.room.cart.ListCheckout
import com.google.gson.Gson

class ProductType : NavType<ListCheckout>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): ListCheckout? {
        return if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.TIRAMISU){
            bundle.getParcelable(key, ListCheckout::class.java)
        }else {
            @Suppress("DEPRECATION")
            return bundle.getParcelable(key)
        }
    }
    override fun parseValue(value: String): ListCheckout{
        return Gson().fromJson(value, ListCheckout::class.java)
    }
    override fun put(bundle: Bundle, key: String, value: ListCheckout) {
        bundle.putParcelable(key, value)
    }
}