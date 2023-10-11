package com.example.core.api.response

import com.google.gson.annotations.SerializedName

data class RefreshResponse(
    @SerializedName("code")
    var code: Int,
    @SerializedName("message")
    var message: String,
    @SerializedName("data")
    var data: Data
) {
    data class Data(
        @SerializedName("accessToken")
        var accessToken: String,
        @SerializedName("refreshToken")
        var refreshToken: String,
        @SerializedName("expiresAt")
        var expiresAt: Long
    )
}