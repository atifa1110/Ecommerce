package com.example.ecommerce.room.cart

import android.util.Log
import com.example.ecommerce.api.model.ProductDetail
import com.example.ecommerce.api.model.ProductVariant
import com.example.ecommerce.api.response.DetailResponse
import com.example.ecommerce.room.favorite.Favorite
import com.example.ecommerce.room.favorite.toEntityCart
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class CartRepositoryImpl(
    private val localDataSource: CartLocalDataSource
): CartRepository {

    override suspend fun addProductsToCart(productDetail: ProductDetail,
        productVariant: ProductVariant) {

        CoroutineScope(Dispatchers.IO).launch {
            //search for id
            val cart = localDataSource.findById(productDetail.productId!!)
            //if it's null than add to data
            if (cart == null) {
                Log.d("CartRepositoryImpl", "Success Add to cart")
                localDataSource.addToCart(productDetail.toEntity(productVariant))
            }
            else {
                // if it's not empty than update cart
                Log.d("CartRepositoryImpl", "Success Update to cart")
                var quantity = productDetail.toEntity(productVariant).quantity
                Log.d("QuantityCart", quantity.toString())
                localDataSource.update(productDetail.productId, quantity!! + 1)
            }
        }
    }

    override suspend fun addFavoriteToCart(favorite: Favorite) {
        CoroutineScope(Dispatchers.IO).launch {
            val cart = localDataSource.findById(favorite.productId)
            if(cart == null){
                localDataSource.addToCart(favorite.toEntityCart())
                Log.d("CartRepositoryImpl", "Success Add to cart")
            }else{
                localDataSource.update(favorite.productId, favorite.quantity!!+1)
            }
        }
    }

    override suspend fun selected(id: String, selected: Boolean) {
        localDataSource.selected(id,selected)
    }

    override suspend fun selectedAll(selected: Boolean) {
        localDataSource.selectedAll(selected)
    }

    override suspend fun getTotal(): Int {
        val list = localDataSource.getSelected()
        return if(list.isNotEmpty()){
            localDataSource.getTotal()
        }else{
            0
        }
    }

    override fun getAllSelected(): List<Cart> {
        return localDataSource.getAllSelected()
    }

    override fun getAllCarts(): Flow<List<Cart>>
    = localDataSource.getAllCart()
    
    override suspend fun deleteById(id: String) {
        return localDataSource.deleteById(id)
    }

    override suspend fun deletedBySelected() {
        return localDataSource.deletedBySelected()
    }

    override suspend fun deleteAllCart() {
        return localDataSource.deleteAllCart()
    }

    override suspend fun updateAddQuantity(cart: Cart, quantity: Int) =
        if(cart.quantity==0){
            localDataSource.deleteById(cart.productId)
        }else {
            localDataSource.update(cart.productId, quantity)
        }

    override suspend fun checkSelected(): Boolean {
        val result = localDataSource.getAllSelected()
        return if(result.size>=1) true else false
    }
}