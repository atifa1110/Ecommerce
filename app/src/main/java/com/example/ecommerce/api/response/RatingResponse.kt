package com.example.ecommerce.api.response

import com.google.gson.annotations.SerializedName

data class RatingResponse(
    @SerializedName("code")
    var code: Int,
    @SerializedName("message")
    var message: String
)