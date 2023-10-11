package com.example.core.room.notification

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val notificationDao: NotificationDao
) : NotificationRepository {

    override fun addNotification(notification: Notification) {
        return notificationDao.addNotification(notification)
    }

    override fun getNotification(): Flow<List<Notification>> {
        return notificationDao.getAllNotification()
    }

    override suspend fun getNotificationRead(read: Boolean): Int {
        val data = notificationDao.getNotificationRead(read).first()
        return if (data.isNotEmpty()) data.size else 0
    }

    override suspend fun updateReadNotification(id: Int, read: Boolean) {
        return notificationDao.updateReadNotification(id, read)
    }

}