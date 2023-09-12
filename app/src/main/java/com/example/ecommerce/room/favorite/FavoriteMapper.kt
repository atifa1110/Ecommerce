package com.example.ecommerce.room.favorite

import com.example.ecommerce.api.model.ProductDetail
import com.example.ecommerce.api.model.ProductVariant
import com.example.ecommerce.api.response.DetailResponse
import com.example.ecommerce.room.cart.Cart

fun ProductDetail.toEntity(variant : ProductVariant): Favorite{
    var variantName : String? = ""
    var variantPrice : Int? = 0
    productVariant!!.forEach {
        variantName = it.variantName
        variantPrice = it.variantPrice
    }
    return Favorite(
        productId = productId!!,
        productName = productName,
        productPrice = productPrice,
        image = image?.get(0),
        brand = brand,
        description = description,
        store = store,
        sale = sale,
        stock = stock,
        totalRating = totalRating,
        totalReview = totalReview,
        totalSatisfaction = totalSatisfaction,
        productRating = productRating,
        productVariantName = variant.variantName,
        productVariantPrice = variant.variantPrice,
        quantity = 1,
        favorite = false
    )
}

fun Favorite.toEntityCart(): Cart{
    return Cart(
        productId = productId,
        productName = productName,
        productPrice = productPrice,
        image = image,
        brand = brand,
        description = description,
        store = store,
        sale = sale,
        stock = stock,
        totalRating = totalRating,
        totalReview = totalReview,
        totalSatisfaction = totalSatisfaction,
        productRating = productRating,
        productVariantName = productVariantName,
        productVariantPrice = productVariantPrice,
        quantity = 1,
        selected = false
    )
}