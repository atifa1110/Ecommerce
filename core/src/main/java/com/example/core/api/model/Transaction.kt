package com.example.core.api.model

import com.example.core.room.cart.CartItem


data class Transaction(
    val invoiceId: String,
    val status: Boolean,
    val date: String,
    val time: String,
    val payment: String,
    val total: Int,
    val items: List<CartItem>,
    val rating : String,
    val review : String,
    val image : String,
    val name : String
)