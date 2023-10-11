package com.example.core.room.favorite

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FavoriteLocalDataSourceImpl @Inject constructor(
    private val favoriteDao: FavoriteDao
) : FavoriteLocalDataSource {
    override suspend fun addToFavorite(favorite: Favorite) {
        return favoriteDao.addToFavorite(favorite)
    }

    override suspend fun updateFavorite(id: String, favorite: Boolean) {
        return favoriteDao.updateFavorite(id, favorite)
    }

    override suspend fun deleteFavoriteById(id: String) {
        return favoriteDao.deleteFavoriteById(id)
    }

    override fun getAllFavorite(): Flow<List<Favorite>> {
        return favoriteDao.getAllFavorite()
    }

    override fun getFavoriteById(id: String): List<Favorite>{
        return favoriteDao.getFavoriteById(id)
    }

}