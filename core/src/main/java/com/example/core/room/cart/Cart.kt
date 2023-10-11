package com.example.core.room.cart

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "cart")
@Parcelize
data class Cart (
    @PrimaryKey(autoGenerate = false)
    @NonNull
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
    @ColumnInfo("quantity")
    var quantity:Int? = null,
    @ColumnInfo("selected")
    var selected:Boolean? = false
) : Parcelable

@Parcelize
data class ListCheckout(
    val listCheckout: List<com.example.core.room.cart.Cart> = emptyList()
) : Parcelable