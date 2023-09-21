package com.example.ecommerce.room.notification

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Notification::class], version = 1, exportSchema = false)
abstract class NotificationDatabase : RoomDatabase() {
    abstract fun notificationDao(): NotificationDao
}