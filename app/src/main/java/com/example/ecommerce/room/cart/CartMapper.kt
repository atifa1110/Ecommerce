package com.example.ecommerce.room.cart

import com.example.ecommerce.api.model.ProductDetail
import com.example.ecommerce.api.model.ProductVariant

fun ProductDetail.toEntity(variant : ProductVariant): Cart {
    return Cart(
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
        selected = false
    )
}
