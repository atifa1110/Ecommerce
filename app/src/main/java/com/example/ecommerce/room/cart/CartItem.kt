package com.example.ecommerce.room.cart

import com.google.gson.annotations.SerializedName

data class CartItem (
    var productId: String? = null,
    val variantName:String? = null,
    var quantity:Int? = null,
)