package com.example.ecommerce.api.response

import com.example.ecommerce.api.model.Product
import com.example.ecommerce.api.model.ProductVariant
import com.google.gson.annotations.SerializedName
import java.util.UUID

data class DetailResponse (
    @SerializedName("code")
    var code: Int,
    @SerializedName("message")
    var message: String,
    @SerializedName("data")
    var data : ProductDetail
){
    data class ProductDetail(
        @SerializedName("productId")
        val productId: String? = null,
        @SerializedName("productName")
        val productName: String? = null,
        @SerializedName("productPrice")
        val productPrice: Int? = null,
        @SerializedName("image")
        val image: List<String>? = null,
        @SerializedName("brand")
        val brand: String? = null,
        @SerializedName("description")
        val description: String? = null,
        @SerializedName("store")
        val store: String? = null,
        @SerializedName("sale")
        val sale: Int? = null,
        @SerializedName("stock")
        val stock: Int? = null,
        @SerializedName("totalRating")
        val totalRating: Int? = null,
        @SerializedName("totalReview")
        val totalReview: Int? = null,
        @SerializedName("totalSatisfaction")
        val totalSatisfaction: Int? = null,
        @SerializedName("productRating")
        val productRating: Float? = null,
        @SerializedName("productVariant")
        val productVariant: List<ProductVariant>? = null
    )
}