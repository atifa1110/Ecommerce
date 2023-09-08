package com.example.ecommerce.api.model

import com.google.gson.annotations.SerializedName

data class Payment(
    @SerializedName("title")
    val title : String,
    @SerializedName("item")
    val item : List<Item>
)

data class Item (
    @SerializedName("label")
    val label : String,
    @SerializedName("image")
    val image : String,
    @SerializedName("status")
    val status : Boolean
)
