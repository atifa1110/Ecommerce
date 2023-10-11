package com.example.ecommerce.room.cart

import com.example.core.api.model.ProductDetail
import com.example.core.api.model.ProductVariant
import com.example.core.room.cart.Cart
import com.example.core.room.favorite.Favorite
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    suspend fun addProductsToCart(
        productDetail: ProductDetail,
        productVariant: ProductVariant
    )

    suspend fun addFavoriteToCart(favorite: Favorite)

    suspend fun getTotal(): Int

    fun getAllSelected(): Flow<List<Cart>>

    fun getAllCarts(): Flow<List<Cart>>

    suspend fun deleteById(id: String)

    suspend fun deletedBySelected()

    suspend fun deleteAllCart()

    suspend fun updateQuantity(cart: Cart, quantity: Int)

    suspend fun updateChecked(id: String, selected: Boolean)

    suspend fun updateCheckedAll(selected: Boolean)

    suspend fun checkSelected(): Boolean
}
