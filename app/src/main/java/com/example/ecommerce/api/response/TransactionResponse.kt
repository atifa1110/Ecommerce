package com.example.ecommerce.api.response

import com.example.ecommerce.api.model.Transaction
import com.google.gson.annotations.SerializedName

data class TransactionResponse (
    @SerializedName("code")
    var code: Int,
    @SerializedName("message")
    var message: String,
    @SerializedName("data")
    var data : List<Transaction>
)