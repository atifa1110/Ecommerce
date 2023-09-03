package com.example.ecommerce.api.model

data class Filter (
    val search: String? = null,
    val brand : String? = null,
    val lowest: Int? = null,
    val highest : Int? = null,
    val sort: String? = null,
)