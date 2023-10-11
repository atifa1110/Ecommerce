package com.example.core.api.model

data class ProductFilter (
    val search: String? = null,
    val brand : String? = null,
    val lowest: Int? = null,
    val highest : Int? = null,
    val sort: String? = null,
)