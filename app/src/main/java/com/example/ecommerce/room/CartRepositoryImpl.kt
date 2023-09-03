package com.example.ecommerce.room

import javax.inject.Inject

class ProductsCartRepositoryImpl @Inject constructor(
    private val productsDao: ProductsDao
) : ProductsCartRepository{
    override suspend fun addProductsToCart(products: Products) {
        return productsDao.addProductsToCart(products)
    }

    override fun getAllProducts(): List<Products> {
        return productsDao.getAllProducts()
    }

}