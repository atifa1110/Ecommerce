package com.example.ecommerce.room.notification

import com.example.ecommerce.room.cart.CartDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NotificationRepositoryImpl  @Inject constructor(
    private val notificationDao: NotificationDao
) : NotificationRepository{

    override fun addNotification(notification: Notification) {
        notificationDao.addNotification(notification)
    }

    override fun getNotification(): Flow<List<Notification>> {
        return notificationDao.getAllNotification()
    }

    override suspend fun updateReadNotification(id: Int, read: Boolean) {
        return notificationDao.updateReadNotification(id,read)
    }

}