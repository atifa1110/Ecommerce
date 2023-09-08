package com.example.ecommerce.api.response

import com.google.gson.annotations.SerializedName

data class ReviewResponse (
    @SerializedName("code")
    var code: Int,
    @SerializedName("message")
    var message: String,
    @SerializedName("data")
    var data : List<Review>?
) {
    data class Review(
        @SerializedName("userName")
        var userName: String?=null,
        @SerializedName("userImage")
        var userImage: String?=null,
        @SerializedName("userRating")
        var userRating: Int?=null,
        @SerializedName("userReview")
        var userReview: String?=null,
    )
}