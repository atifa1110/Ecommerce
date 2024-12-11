package com.example.ecommerce.room.cart

import android.util.Log
import com.example.core.api.model.ProductDetail
import com.example.core.api.model.ProductVariant
import com.example.core.room.cart.Cart
import com.example.core.room.cart.toEntity
import com.example.core.room.favorite.Favorite
import com.example.core.room.favorite.toEntityCart
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class CartRepositoryImpl(
    private val localDataSource: com.example.core.room.cart.CartLocalDataSource
) : CartRepository {

    override suspend fun addProductsToCart(
        productDetail: ProductDetail,
        productVariant: ProductVariant
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            // search for id
            val cart = localDataSource.findById(productDetail.productId!!)
            // if it's null than add to data
            if (cart == null) {
                Log.d("CartRepositoryImpl", "Success Add to cart")
                localDataSource.addToCart(productDetail.toEntity(productVariant))
            } else {
                // if it's not empty than update cart
                Log.d("CartRepositoryImpl", "Success Update to cart")
                var quantity = productDetail.toEntity(productVariant).quantity
                Log.d("QuantityCart", quantity.toString())
                localDataSource.updateQuantity(productDetail.productId ?: "", quantity)
            }
        }
    }

    override suspend fun addFavoriteToCart(favorite: Favorite) {
        CoroutineScope(Dispatchers.IO).launch {
            val cart = localDataSource.findById(favorite.productId)
            if (cart.productId.isEmpty()) {
                localDataSource.addToCart(favorite.toEntityCart())
            } else {
                localDataSource.updateQuantity(favorite.productId, favorite.quantity)
            }
        }
    }

    override suspend fun getTotal(): Int {
        val list = localDataSource.getSelected().first()
        return if (list.isNotEmpty()) {
            localDataSource.getTotal()
        } else {
            0
        }
    }

    override fun getAllSelected(): Flow<List<Cart>> {
        return localDataSource.getSelected()
    }

    override fun getAllCarts(): Flow<List<Cart>> =
        localDataSource.getAllCart()

    override suspend fun deleteById(id: String) {
        return localDataSource.deleteById(id)
    }

    override suspend fun deletedBySelected() {
        return localDataSource.deletedBySelected()
    }

    override suspend fun deleteAllCart() {
        return localDataSource.deleteAllCart()
    }

    override suspend fun updateQuantity(cart: com.example.core.room.cart.Cart, quantity: Int) =
        if (cart.quantity == 0) {
            localDataSource.deleteById(cart.productId)
        } else {
            localDataSource.updateQuantity(cart.productId, quantity)
        }

    override suspend fun updateChecked(id: String, selected: Boolean) {
        localDataSource.updateChecked(id, selected)
    }

    override suspend fun updateCheckedAll(selected: Boolean) {
        localDataSource.updateCheckedAll(selected)
    }

    override suspend fun checkSelected(): Boolean {
        val result = localDataSource.getSelected().first()
        return if (result.size >= 1) true else false
    }
}
