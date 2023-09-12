package com.example.ecommerce.api.model

data class Fulfillment(
    val invoiceId : String,
    val status : Boolean,
    val date : String,
    val time : String,
    val payment : String,
    val total : Int
)
