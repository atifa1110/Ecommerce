package com.example.core.api.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Payment(
    @SerializedName("title")
    val title: String,
    @SerializedName("item")
    val item: List<Item>
) : Parcelable

@Parcelize
data class Item (
    @SerializedName("label")
    val label : String?=null,
    @SerializedName("image")
    val image : String?= null,
    @SerializedName("status")
    val status : Boolean?=null
) : Parcelable

