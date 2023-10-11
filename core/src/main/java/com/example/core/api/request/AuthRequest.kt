package com.example.core.api.request

import com.google.gson.annotations.SerializedName

data class AuthRequest(
    @SerializedName("email")
    var email: String,
    @SerializedName("password")
    var password: String,
    @SerializedName("firebaseToken")
    var firebaseToken: String
)