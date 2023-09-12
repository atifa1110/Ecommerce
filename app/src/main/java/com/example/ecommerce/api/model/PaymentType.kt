package com.example.ecommerce.api.model

import android.os.Build
import android.os.Bundle
import androidx.navigation.NavType
import com.google.gson.Gson

class PaymentType : NavType<Item>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): Item? {
        return if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.TIRAMISU){
            bundle.getParcelable(key, Item::class.java)
        }else {
            @Suppress("DEPRECATION")
            return bundle.getParcelable(key)
        }
    }
    override fun parseValue(value: String): Item {
        return Gson().fromJson(value, Item::class.java)
    }
    override fun put(bundle: Bundle, key: String, value: Item) {
        bundle.putParcelable(key, value)
    }
}