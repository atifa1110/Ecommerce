package com.example.ecommerce.room.notification

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.ecommerce.room.cart.Cart
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addNotification(notification: Notification)

    @Query("SELECT * FROM notification")
    fun getAllNotification(): Flow<List<Notification>>

    @Query("UPDATE notification SET isRead = :read WHERE id = :id")
    suspend fun updateReadNotification(id: Int, read: Boolean)

}