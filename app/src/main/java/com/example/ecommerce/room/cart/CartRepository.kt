package com.example.ecommerce.room.cart

import com.example.ecommerce.api.model.ProductDetail
import com.example.ecommerce.api.model.ProductVariant
import com.example.ecommerce.api.response.DetailResponse
import com.example.ecommerce.room.cart.Cart
import com.example.ecommerce.room.favorite.Favorite
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    suspend fun addProductsToCart(
        productDetail: ProductDetail,
        productVariant: ProductVariant
    )

    suspend fun addFavoriteToCart(favorite : Favorite)

    suspend fun selected(id: String,selected: Boolean)

    suspend fun selectedAll(selected: Boolean)

    suspend fun getTotal(): Int

    fun getAllSelectedList() : List<Cart>

    fun getAllSelected() : Flow<List<Cart>>

    fun getAllCarts() : Flow<List<Cart>>

    suspend fun deleteById(id: String)

    suspend fun deletedBySelected()

    suspend fun deleteAllCart()

    suspend fun updateAddQuantity(cart : Cart, quantity:Int)

    suspend fun checkSelected() : Boolean
}