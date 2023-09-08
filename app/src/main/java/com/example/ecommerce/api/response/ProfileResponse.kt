package com.example.ecommerce.api.response

import com.google.gson.annotations.SerializedName

data class ProfileResponse(
    @SerializedName("code")
    var code: Int,
    @SerializedName("message")
    var message: String,
    @SerializedName("data")
    var data : Data
) {
    data class Data(
        @SerializedName("userName")
        var userName: String,
        @SerializedName("userImage")
        var userImage: String
    )
}