package com.example.core.api.model

import android.os.Build
import android.os.Bundle
import androidx.navigation.NavType
import com.google.gson.Gson

class FulfillmentType : NavType<Fulfillment>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): Fulfillment? {
        return if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.TIRAMISU){
            bundle.getParcelable(key, Fulfillment::class.java)
        }else {
            @Suppress("DEPRECATION")
            return bundle.getParcelable(key)
        }
    }
    override fun parseValue(value: String): Fulfillment {
        return Gson().fromJson(value, Fulfillment::class.java)
    }
    override fun put(bundle: Bundle, key: String, value: Fulfillment) {
        bundle.putParcelable(key, value)
    }
}