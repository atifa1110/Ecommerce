package com.example.ecommerce.api.response

import com.example.ecommerce.api.model.Product
import com.example.ecommerce.api.model.ProductDetail
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
)