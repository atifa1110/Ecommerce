package com.example.ecommerce.databaseTest

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.core.room.notification.Notification
import com.example.core.room.notification.NotificationDao
import com.example.core.room.notification.NotificationDatabase
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class NotificationDatabaseTest {

    private lateinit var database: NotificationDatabase
    private lateinit var dao: NotificationDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database =
            Room.inMemoryDatabaseBuilder(context, NotificationDatabase::class.java)
                .allowMainThreadQueries()
                .build()
        dao = database.notificationDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun `test add notification room`() {
        runBlocking {
            val notification = Notification(
                1,
                "Telkomsel Award 2023",
                "Nikmati kemeriahan ulang ",
                "image",
                "promo",
                "21 Jul 2023",
                "12:34",
                false
            )

            dao.addNotification(notification)
            val retrieve = dao.getAllNotification().first()
            assertEquals(retrieve.isNotEmpty(), true)
        }
    }

    @Test
    fun `test get all notification room`() {
        runBlocking {
            val notification = Notification(
                1,
                "Telkomsel Award 2023",
                "Nikmati kemeriahan ulang ",
                "image",
                "promo",
                "21 Jul 2023",
                "12:34",
                false
            )

            val notification1 = Notification(
                2,
                "Telkomsel Award 2023",
                "Nikmati kemeriahan ulang ",
                "image",
                "promo",
                "21 Jul 2023",
                "12:34",
                false
            )

            dao.addNotification(notification)
            dao.addNotification(notification1)
            val retrieve = dao.getAllNotification().first()
            assertEquals(retrieve.isNotEmpty(), true)
        }
    }

    @Test
    fun `test update read room`() {
        runBlocking {
            val notification = Notification(
                1,
                "Telkomsel Award 2023",
                "Nikmati kemeriahan ulang ",
                "image",
                "promo",
                "21 Jul 2023",
                "12:34",
                false
            )

            val notification1 = Notification(
                2,
                "Telkomsel Award 2023",
                "Nikmati kemeriahan ulang ",
                "image",
                "promo",
                "21 Jul 2023",
                "12:34",
                false
            )

            dao.addNotification(notification)
            dao.addNotification(notification1)
            dao.updateReadNotification(1, true)

            val retrieve = dao.getAllNotification().first()
            assertEquals(retrieve.any { it.isRead }, true)
        }
    }
}
