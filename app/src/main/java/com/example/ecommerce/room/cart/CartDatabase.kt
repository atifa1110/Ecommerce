package com.example.ecommerce.room.cart

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.ecommerce.room.favorite.Favorite
import com.example.ecommerce.room.favorite.FavoriteDao

@Database(entities = [Cart::class, Favorite::class], version = 1, exportSchema = false)
abstract class CartDatabase : RoomDatabase() {
    abstract fun cartDao(): CartDao

    abstract fun FavoriteDao() : FavoriteDao
}