package com.example.core.room.cart

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CartLocalDataSourceImpl @Inject constructor(
    private val cartDao: CartDao
) : CartLocalDataSource {
    override suspend fun addToCart(cart: Cart) {
        return cartDao.addToCart(cart)
    }

    override fun findById(id: String): Cart {
        return cartDao.findById(id)
    }

    override suspend fun getTotal(): Int {
        return cartDao.getTotal()
    }

    override fun getAllCart(): Flow<List<Cart>> {
        return cartDao.getAllCart()
    }

    override fun getSelected(): Flow<List<Cart>> {
        return cartDao.getSelected()
    }

    override suspend fun deleteById(id: String) {
        return cartDao.deleteById(id)
    }

    override suspend fun deletedBySelected() {
        return cartDao.deleteBySelected()
    }


    override suspend fun deleteAllCart() {
        return cartDao.deleteAllCart()
    }

    override suspend fun updateQuantity(id: String, quantity: Int?) {
        return cartDao.updateQuantity(id, quantity)
    }

    override suspend fun updateChecked(id: String, selected: Boolean) {
        return cartDao.updateChecked(id, selected)
    }

    override suspend fun updateCheckedAll(selected: Boolean) {
        return cartDao.updateCheckedAll(selected)
    }

}