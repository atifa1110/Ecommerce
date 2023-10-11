package com.example.ecommerce.repositoryTest

import com.example.core.api.method.FirebaseService
import com.example.core.api.response.FirebaseResponse
import com.example.core.firebase.MessagingRepository
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import retrofit2.Response

class MessagingRepositoryTest {

    private lateinit var apiFirebaseService: FirebaseService
    private lateinit var messagingRepository: MessagingRepository

    @Before
    fun setup() {
        apiFirebaseService = mock()
        messagingRepository = MessagingRepository(apiFirebaseService)
    }

    @Test
    fun `test firebase token repository`() {
        runBlocking {
            val expected = FirebaseResponse(
                200,
                "Firebase token updated",
            )
            whenever(
                apiFirebaseService.firebaseToken("123")
            ).thenReturn(Response.success(expected))

            val result = messagingRepository.firebaseToken("123").body()

            assertEquals(expected, result)
        }
    }
}
