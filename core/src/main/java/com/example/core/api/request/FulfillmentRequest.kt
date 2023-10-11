package com.example.core.api.request

import com.example.core.room.cart.CartItem
import com.google.gson.annotations.SerializedName

data class FulfillmentRequest (
    @SerializedName("payment")
    var payment: String?,
    @SerializedName("items")
    var items: List<CartItem>?,
)