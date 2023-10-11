package com.example.core.api.model

data class Product(
    val productId : String = "",
    val productName : String = "",
    val productPrice : Int = 0,
    val image : String = "",
    val brand : String = "",
    val store : String = "",
    val sale : Int = 0,
    val productRating : Float = 0F
)