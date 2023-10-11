package com.example.core.room.cart

data class CartItem (
    var productId: String? = null,
    val variantName:String? = null,
    var quantity:Int? = null,
)