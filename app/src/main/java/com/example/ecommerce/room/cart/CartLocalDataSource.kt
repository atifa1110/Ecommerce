package com.example.ecommerce.room

import kotlinx.coroutines.flow.Flow

interface CartLocalDataSource {
    suspend fun addToCart(cart: Cart)

    suspend fun selected(id: String,selected: Boolean)

    suspend fun selectedAll(selected: Boolean)

    fun findById(id: String): Cart

    suspend fun getSelected() : List<Cart>
    suspend fun getTotal(): Int

    fun getAllCart(): Flow<List<Cart>>

    suspend fun deleteById(id: String)

    suspend fun deletedBySelected()
    suspend fun deleteAllCart()

    suspend fun update(id: String, quantity: Int?)

}