package com.example.core.api.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("code")
    var code: Int,
    @SerializedName("message")
    var message: String,
    @SerializedName("data")
    var data : LoginData
) {
    data class LoginData(
        @SerializedName("userName")
        var userName: String,
        @SerializedName("userImage")
        var userImage: String,
        @SerializedName("accessToken")
        var accessToken: String,
        @SerializedName("refreshToken")
        var refreshToken: String,
        @SerializedName("expiresAt")
        var expiresAt: Long
    )
}