package com.example.core.api.response

import com.example.core.api.model.Product
import com.google.gson.annotations.SerializedName

data class ProductResponse (
    @SerializedName("code")
    var code: Int,
    @SerializedName("message")
    var message: String,
    @SerializedName("data")
    var data : Data
){
    data class Data(
        @SerializedName("itemsPerPage")
        val itemsPerPage: Int,
        @SerializedName("currentItemCount")
        val currentItemCount: Int,
        @SerializedName("pageIndex")
        val pageIndex: Int,
        @SerializedName("totalPages")
        val totalPages: Int,
        @SerializedName("items")
        val items: ArrayList<Product>
    )
}