package com.example.ecommerce.room.cart

import com.example.ecommerce.api.model.ProductVariant
import com.example.ecommerce.api.response.DetailResponse

fun DetailResponse.ProductDetail.toEntity(variant : ProductVariant): Cart {
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

fun Cart.toProductDetail(): DetailResponse.ProductDetail{
    return DetailResponse.ProductDetail(
        productId = productId,
        productName = productName,
        productPrice = productPrice,
        brand = brand,
        description = description,
        store = store,
        sale = sale,
        stock = stock,
        totalRating = totalRating,
        totalReview = totalReview,
        totalSatisfaction = totalSatisfaction,
        productRating = productRating,
    )
}