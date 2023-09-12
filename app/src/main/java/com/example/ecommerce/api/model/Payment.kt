package com.example.ecommerce.api.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class Payment(
    @SerializedName("title")
    val title : String,
    @SerializedName("item")
    val item : List<Item>
)

@Parcelize
data class Item (
    @SerializedName("label")
    val label : String?=null,
    @SerializedName("image")
    val image : String?= null,
    @SerializedName("status")
    val status : Boolean?=null
) : Parcelable
