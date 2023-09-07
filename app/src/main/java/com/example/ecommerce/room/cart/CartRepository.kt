package com.example.ecommerce.room

import com.example.ecommerce.api.model.ProductVariant
import com.example.ecommerce.api.response.DetailResponse
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    suspend fun addProductsToCart(
        productDetail: DetailResponse.ProductDetail,
        productVariant: ProductVariant
    )

    suspend fun selected(id: String,selected: Boolean)

    suspend fun selectedAll(selected: Boolean)

    suspend fun getTotal(): Int

    fun getAllCarts() : Flow<List<Cart>>

    suspend fun deleteById(id: String)

    suspend fun deletedBySelected()

    suspend fun deleteAllCart()

    suspend fun updateAddQuantity(cart : Cart,quantity:Int)
}