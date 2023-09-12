package com.example.ecommerce.api.response

import com.example.ecommerce.api.model.Fulfillment
import com.google.gson.annotations.SerializedName

data class FulfillmentResponse (
    @SerializedName("code")
    var code: Int,
    @SerializedName("message")
    var message: String,
    @SerializedName("data")
    var data : Fulfillment
)