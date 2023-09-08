package com.example.ecommerce.api.response

import com.example.ecommerce.api.model.Payment
import com.google.gson.annotations.SerializedName

data class PaymentResponse (
    @SerializedName("code")
    var code: Int,
    @SerializedName("message")
    var message: String,
    @SerializedName("data")
    var data : List<Payment>
)