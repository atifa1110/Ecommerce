package com.example.core.room.favorite

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToFavorite(favorite: Favorite)

    @Query("DELETE FROM favorite WHERE productId = :id")
    suspend fun deleteFavoriteById(id: String)

    @Query("SELECT * FROM favorite")
    fun getAllFavorite(): Flow<List<Favorite>>

    @Query("SELECT * FROM favorite WHERE productId = :id")
    fun getFavoriteById(id: String): List<Favorite>

    @Query("UPDATE favorite SET favorite  = :favorite WHERE productId = :id")
    suspend fun updateFavorite(id:String,favorite: Boolean)

}