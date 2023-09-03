package com.example.ecommerce.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Products::class], version = 1, exportSchema = false)
abstract class ProductsDatabase : RoomDatabase() {
    abstract fun productsDao(): ProductsDao
}