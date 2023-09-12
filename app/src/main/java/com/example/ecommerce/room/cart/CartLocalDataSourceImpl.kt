package com.example.ecommerce.room.cart

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CartLocalDataSourceImpl @Inject constructor(
    private val cartDao: CartDao
) : CartLocalDataSource {
    override suspend fun addToCart(cart: Cart) {
        return cartDao.addToCart(cart)
    }

    override suspend fun selected(id: String, selected: Boolean) {
        return cartDao.updateChecked(id,selected)
    }

    override suspend fun selectedAll(selected: Boolean) {
        return cartDao.updateCheckedAll(selected)
    }

    override fun findById(id: String): Cart {
        return cartDao.findById(id)
    }

    override suspend fun getSelected(): List<Cart> {
        return cartDao.getSelected()
    }

    override suspend fun getTotal(): Int {
        return cartDao.getTotal()
    }

    override fun getAllCart(): Flow<List<Cart>> {
        return cartDao.getAllCart()
    }

    override fun getAllSelectedList(): List<Cart> {
        return cartDao.getAllSelectedList()
    }

    override fun getAllSelected(): Flow<List<Cart>> {
        return cartDao.getAllSelected()
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

    override suspend fun update(id: String, quantity: Int?) {
        return cartDao.update(id,quantity)
    }
}