package com.example.ecommerce.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface ProductsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addProductsToCart(products: Products)
    @Query("SELECT * FROM products")
    fun getAllProducts(): List<Products>
}