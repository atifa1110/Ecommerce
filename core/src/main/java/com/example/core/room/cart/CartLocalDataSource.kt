package com.example.core.room.cart

import kotlinx.coroutines.flow.Flow

interface CartLocalDataSource {
    suspend fun addToCart(cart: Cart)

    fun findById(id: String): Cart

    suspend fun getTotal(): Int

    fun getAllCart(): Flow<List<Cart>>

    fun getSelected(): Flow<List<Cart>>

    suspend fun deleteById(id: String)

    suspend fun deletedBySelected()

    suspend fun deleteAllCart()

    suspend fun updateQuantity(id: String, quantity: Int?)

    suspend fun updateChecked(id: String, selected: Boolean)

    suspend fun updateCheckedAll(selected: Boolean)

}