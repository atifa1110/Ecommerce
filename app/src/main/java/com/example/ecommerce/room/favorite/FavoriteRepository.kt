package com.example.ecommerce.room.favorite

import com.example.ecommerce.api.model.ProductDetail
import com.example.ecommerce.api.model.ProductVariant
import com.example.ecommerce.api.response.DetailResponse
import com.example.ecommerce.room.cart.Cart
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {

    suspend fun addProductsToFavorite(
        productDetail: ProductDetail,
        productVariant: ProductVariant
    )

    suspend fun deleteFavoriteById(id:String)

    fun getAllFavorite() : Flow<List<Favorite>>

    fun getFavoriteById(id:String) : List<Favorite>
}