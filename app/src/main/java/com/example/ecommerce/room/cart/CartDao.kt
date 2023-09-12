package com.example.ecommerce.room.cart

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToCart(cart: Cart)

    @Query("SELECT * FROM cart WHERE productId = :id")
    fun findById(id: String): Cart

    @Query("SELECT * FROM cart Where selected = 1")
    suspend fun getSelected() : List<Cart>

    @Query("SELECT * FROM cart Where selected = 1")
    fun getAllSelectedList() : List<Cart>

    @Query("SELECT * FROM cart Where selected = 1")
    fun getAllSelected() : Flow<List<Cart>>

    @Query("SELECT SUM(quantity * productPrice) FROM cart WHERE selected = 1")
    suspend fun getTotal(): Int

    @Query("SELECT * FROM cart")
    fun getAllCart(): Flow<List<Cart>>

    @Query("DELETE FROM cart WHERE productId = :id")
    suspend fun deleteById(id: String)

    @Query("DELETE FROM cart")
    suspend fun deleteAllCart()

    @Query("DELETE FROM cart WHERE selected = 1")
    suspend fun deleteBySelected()

    @Query("UPDATE cart SET quantity = :quantity WHERE productId = :id")
    suspend fun update(id: String, quantity: Int?)

    @Query("UPDATE cart SET selected = :selected WHERE productId = :id")
    suspend fun updateChecked(id: String, selected: Boolean)

    @Query("UPDATE cart SET selected = :selected")
    suspend fun updateCheckedAll(selected: Boolean)
}