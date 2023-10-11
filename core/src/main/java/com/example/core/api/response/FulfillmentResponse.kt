package com.example.core.api.response

import com.example.core.api.model.Fulfillment
import com.google.gson.annotations.SerializedName

data class FulfillmentResponse (
    @SerializedName("code")
    var code: Int,
    @SerializedName("message")
    var message: String,
    @SerializedName("data")
    var data : Fulfillment
)