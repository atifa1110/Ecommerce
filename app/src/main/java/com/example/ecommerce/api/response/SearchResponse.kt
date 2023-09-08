package com.example.ecommerce.api.response

import com.google.gson.annotations.SerializedName

data class SearchResponse(
    @SerializedName("code")
    var code: Int,
    @SerializedName("message")
    var message: String,
    @SerializedName("data")
    var data : ArrayList<String>
)