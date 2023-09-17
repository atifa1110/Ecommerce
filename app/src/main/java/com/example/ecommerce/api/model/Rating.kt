package com.example.ecommerce.api.model

import retrofit2.http.Query

data class Rating(
    val invoiceId : String,
    val rating : Int,
    val review : String
)
