package com.example.ecommerce.room.favorite

import com.example.core.api.model.ProductDetail
import com.example.core.api.model.ProductVariant
import com.example.core.room.favorite.Favorite
import com.example.core.room.favorite.FavoriteLocalDataSource
import com.example.core.room.favorite.toEntity
import kotlinx.coroutines.flow.Flow

class FavoriteRepositoryImpl(
    private val localDataSource: FavoriteLocalDataSource
) : FavoriteRepository {
    override suspend fun addProductsToFavorite(
        productDetail: ProductDetail,
        productVariant: ProductVariant
    ) {
        localDataSource.addToFavorite(productDetail.toEntity(productVariant))
    }

    override suspend fun deleteFavoriteById(id: String) {
        localDataSource.deleteFavoriteById(id)
    }

    override fun getAllFavorite(): Flow<List<Favorite>> {
        return localDataSource.getAllFavorite()
    }

    override fun getFavoriteById(id: String): List<Favorite> {
        return localDataSource.getFavoriteById(id)
    }
}
