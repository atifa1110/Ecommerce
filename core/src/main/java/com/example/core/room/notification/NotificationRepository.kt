package com.example.core.room.notification

import kotlinx.coroutines.flow.Flow

interface NotificationRepository {

    fun addNotification(notification: Notification)

    fun getNotification(): Flow<List<Notification>>

    suspend fun getNotificationRead(read: Boolean): Int

    suspend fun updateReadNotification(id: Int, read: Boolean)

}