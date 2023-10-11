package com.example.ecommerce.repositoryTest

import com.example.core.room.notification.Notification
import com.example.core.room.notification.NotificationDao
import com.example.core.room.notification.NotificationRepositoryImpl
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class NotificationRepositoryImplTest {

    private lateinit var notificationDao: NotificationDao
    private lateinit var notificationRepositoryImpl: NotificationRepositoryImpl

    @Before
    fun setup() {
        notificationDao = mock()
        notificationRepositoryImpl = NotificationRepositoryImpl(notificationDao)
    }

    @Test
    fun `test add notification repository`() {
        runBlocking {
//            whenever(
//                notificationDao.addNotification(
//                    Notification(
//                        1, "notification", "hello", "image", "info", "21 Jul 2023",
//                        "12:34", false
//                    )
//                )
//            ).thenReturn(Unit)

            val result = notificationRepositoryImpl.addNotification(
                Notification(
                    1,
                    "notification",
                    "hello",
                    "image",
                    "info",
                    "21 Jul 2023",
                    "12:34",
                    false
                )
            )

            assertEquals(Unit, result)
        }
    }

    @Test
    fun `test get all notification repository`() {
        runBlocking {
            val expected = flowOf(
                listOf(
                    Notification(
                        1,
                        "notification",
                        "hello",
                        "image",
                        "info",
                        "21 Jul 2023",
                        "12:34",
                        false
                    )
                )
            )
            whenever(notificationDao.getAllNotification())
                .thenReturn(expected)

            val result = notificationRepositoryImpl.getNotification()

            assertEquals(expected, result)
        }
    }

    @Test
    fun `test update read notification repository`() {
        runBlocking {
            whenever(notificationDao.updateReadNotification(1, true))
                .thenReturn(Unit)

            val result = notificationRepositoryImpl.updateReadNotification(1, true)

            assertEquals(Unit, result)
        }
    }
}
