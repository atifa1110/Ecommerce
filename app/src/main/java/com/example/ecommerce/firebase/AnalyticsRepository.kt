package com.example.ecommerce.firebase

import android.os.Bundle
import androidx.paging.compose.LazyPagingItems
import com.example.core.api.model.Fulfillment
import com.example.core.api.model.Product
import com.example.core.api.model.ProductDetail
import com.example.core.room.cart.Cart
import com.google.firebase.analytics.FirebaseAnalytics
import javax.inject.Inject

class AnalyticsRepository @Inject constructor(
    private val analytics: FirebaseAnalytics
) {
    fun loginAnalytics(email: String) {
        val params = Bundle()
        params.putString(FirebaseAnalytics.Param.METHOD, "Login : $email")
        analytics.logEvent(FirebaseAnalytics.Event.LOGIN, params)
    }

    fun registerAnalytics(email: String) {
        val params = Bundle()
        params.putString(FirebaseAnalytics.Param.METHOD, "Register : $email")
        analytics.logEvent(FirebaseAnalytics.Event.SIGN_UP, params)
    }

    fun viewSearchResult(searchResult: String) {
        val params = Bundle()
        params.putString(FirebaseAnalytics.Param.SEARCH_TERM, searchResult)
        analytics.logEvent(FirebaseAnalytics.Event.SEARCH, params)
    }

    fun selectItemFilter(filter: List<String>) {
        val params = Bundle()
        params.putString(FirebaseAnalytics.Param.ITEMS, filter.toString())
        analytics.logEvent(FirebaseAnalytics.Event.SELECT_ITEM, params)
    }

    fun viewItemList(products: LazyPagingItems<Product>) {
        val params = Bundle()
        params.putString(FirebaseAnalytics.Param.ITEMS, products.toString())
        analytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM_LIST, params)
    }

    fun selectItemProduct(productId: String) {
        val params = Bundle()
        params.putString(FirebaseAnalytics.Param.ITEM_LIST_ID, productId)
        analytics.logEvent(FirebaseAnalytics.Event.SELECT_ITEM, params)
    }

    fun viewItem(productDetail: ProductDetail) {
        val params = Bundle()
        params.putString(
            FirebaseAnalytics.Param.ITEMS,
            "View Item Products : ${productDetail.productId}"
        )
        analytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, params)
    }

    fun addToCart(productId: String?) {
        val params = Bundle()
        params.putString(FirebaseAnalytics.Param.ITEMS, "Add To Cart : $productId")
        analytics.logEvent(FirebaseAnalytics.Event.ADD_TO_CART, params)
    }

    fun removeFromCart(productId: String) {
        val params = Bundle()
        params.putString(FirebaseAnalytics.Param.ITEMS, "Remove from Cart : $productId")
        analytics.logEvent(FirebaseAnalytics.Event.REMOVE_FROM_CART, params)
    }

    fun viewCart(cart: List<Cart>) {
        val data = if (cart.isEmpty()) "Empty" else cart[0].productId
        val params = Bundle()
        params.putString(FirebaseAnalytics.Param.ITEMS, "View Cart : $data")
        analytics.logEvent(FirebaseAnalytics.Event.VIEW_CART, params)
    }

    fun addToWishlist(productId: String?) {
        val params = Bundle()
        params.putString(FirebaseAnalytics.Param.ITEMS, "Add To Wishlist : $productId")
        analytics.logEvent(FirebaseAnalytics.Event.ADD_TO_WISHLIST, params)
    }

    fun beginCheckOut(checkOut: List<Cart>) {
        val data = if (checkOut.isEmpty()) "Empty" else checkOut[0].productId
        val params = Bundle()
        params.putString(FirebaseAnalytics.Param.ITEMS, "Begin to Checkout : $data")
        analytics.logEvent(FirebaseAnalytics.Event.BEGIN_CHECKOUT, params)
    }

    fun addPaymentInfo(payment: String) {
        val params = Bundle()
        params.putString(FirebaseAnalytics.Param.ITEMS, "Choose Payment : $payment")
        analytics.logEvent(FirebaseAnalytics.Event.ADD_PAYMENT_INFO, params)
    }

    fun purchase(checkOut: Fulfillment?) {
        val params = Bundle()
        params.putString(FirebaseAnalytics.Param.ITEMS, "Purchase Product : ${checkOut?.invoiceId}")
        analytics.logEvent(FirebaseAnalytics.Event.PURCHASE, params)
    }

    fun buttonClick(buttonName: String) {
        val params = Bundle()
        params.putString("button_name", buttonName)
        analytics.logEvent("button_click", params)
    }
}
