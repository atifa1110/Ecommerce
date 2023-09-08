package com.example.ecommerce.room.favorite

import com.example.ecommerce.room.cart.Cart
import kotlinx.coroutines.flow.Flow

interface FavoriteLocalDataSource{
    suspend fun addToFavorite(favorite: Favorite)

    suspend fun updateFavorite(id: String, favorite: Boolean)

    suspend fun deleteFavoriteById(id:String)

    fun getAllFavorite() : Flow<List<Favorite>>

    fun getFavoriteById(id:String) : List<Favorite>
}