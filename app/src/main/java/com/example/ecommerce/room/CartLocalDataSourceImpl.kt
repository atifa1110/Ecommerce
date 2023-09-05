package com.example.ecommerce.room

import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val cartDao: CartDao
) : CartRepository{
    override suspend fun addProductsToCart(cart: Cart) {
        return cartDao.addProductsToCart(cart)
    }

    override suspend fun getAllCarts(): List<Cart> {
        return cartDao.getAllCart()
    }

    override suspend fun deleteById(id: String) {
        return cartDao.deleteById(id)
    }

}