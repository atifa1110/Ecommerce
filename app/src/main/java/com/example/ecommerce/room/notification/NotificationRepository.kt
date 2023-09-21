package com.example.ecommerce.room.notification

import com.example.ecommerce.api.model.ProductDetail
import com.example.ecommerce.api.model.ProductVariant
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {

    fun addNotification(notification: Notification)

    fun getNotification() : Flow<List<Notification>>

    suspend fun updateReadNotification(id: Int, read: Boolean)

}