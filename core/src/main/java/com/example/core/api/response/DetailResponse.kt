package com.example.core.api.response

import com.example.core.api.model.ProductDetail
import com.google.gson.annotations.SerializedName

data class DetailResponse (
    @SerializedName("code")
    var code: Int,
    @SerializedName("message")
    var message: String,
    @SerializedName("data")
    var data : ProductDetail
)