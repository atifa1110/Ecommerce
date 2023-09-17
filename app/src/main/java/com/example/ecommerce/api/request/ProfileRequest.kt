package com.example.ecommerce.api.request

import com.google.gson.annotations.SerializedName

data class ProfileRequest (
    @SerializedName("userName")
    var userName : String?,
    @SerializedName("userImage")
    var userImage : String?
)