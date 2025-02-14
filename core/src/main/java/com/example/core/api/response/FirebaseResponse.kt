package com.example.core.api.response

import com.google.gson.annotations.SerializedName

data class FirebaseResponse(
    @SerializedName("code")
    var code: Int,
    @SerializedName("message")
    var message: String
)
