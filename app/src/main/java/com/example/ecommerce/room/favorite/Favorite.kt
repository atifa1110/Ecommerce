package com.example.ecommerce.room.favorite

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite")
data class Favorite(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "productId")
    var productId: String,
    @ColumnInfo("productName")
    val productName: String? = null,
    @ColumnInfo("productPrice")
    val productPrice: Int? = null,
    @ColumnInfo("image")
    val image: String?= null,
    @ColumnInfo("brand")
    val brand: String? = null,
    @ColumnInfo("description")
    val description: String? = null,
    @ColumnInfo("store")
    val store: String? = null,
    @ColumnInfo("sale")
    val sale: Int? = null,
    @ColumnInfo("stock")
    val stock: Int? = null,
    @ColumnInfo("totalRating")
    val totalRating: Int? = null,
    @ColumnInfo("totalReview")
    val totalReview: Int? = null,
    @ColumnInfo("totalSatisfaction")
    val totalSatisfaction: Int? = null,
    @ColumnInfo("productRating")
    val productRating: Float? = null,
    @ColumnInfo("productVariantName")
    val productVariantName:String? = null,
    @ColumnInfo("productVariantPrice")
    val productVariantPrice:Int? = null,
    @ColumnInfo("favorite")
    var favorite:Boolean? = false
)