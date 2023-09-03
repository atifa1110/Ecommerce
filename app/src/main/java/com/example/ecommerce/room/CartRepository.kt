package com.example.ecommerce.room

interface ProductsCartRepository {
    suspend fun addProductsToCart(products: Products)

    fun getAllProducts() : List<Products>
}