package com.example.ecommerce.room.favorite

import android.util.Log
import com.example.ecommerce.api.model.ProductDetail
import com.example.ecommerce.api.model.ProductVariant
import com.example.ecommerce.api.response.DetailResponse
import com.example.ecommerce.room.cart.Cart
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class FavoriteRepositoryImpl(
    private val localDataSource: FavoriteLocalDataSource
): FavoriteRepository {
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

    override fun getFavoriteById(id: String) : List<Favorite>{
        return localDataSource.getFavoriteById(id)
    }

}