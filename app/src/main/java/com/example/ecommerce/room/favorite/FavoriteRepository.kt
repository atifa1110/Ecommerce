package com.example.ecommerce.room.favorite

import com.example.core.api.model.ProductDetail
import com.example.core.api.model.ProductVariant
import com.example.core.room.favorite.Favorite
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {

    suspend fun addProductsToFavorite(
        productDetail: ProductDetail,
        productVariant: ProductVariant
    )

    suspend fun deleteFavoriteById(id: String)

    fun getAllFavorite(): Flow<List<Favorite>>

    fun getFavoriteById(id: String): List<Favorite>
}
