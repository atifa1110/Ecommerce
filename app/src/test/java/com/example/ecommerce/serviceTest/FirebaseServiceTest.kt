package com.example.ecommerce.serviceTest

import com.example.core.api.method.FirebaseService
import com.example.core.api.response.FirebaseResponse
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@RunWith(JUnit4::class)
class FirebaseServiceTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var firebaseServiceTest: FirebaseService

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        val client = OkHttpClient.Builder()
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        firebaseServiceTest = retrofit.create(FirebaseService::class.java)
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `test firebase token service`() {
        mockWebServer.enqueueResponse("FirebaseToken.json", 200)
        runBlocking {
            val actual =
                firebaseServiceTest.firebaseToken(
                    "ya29.a0AfB_byCswehJw97NnWMgvi-RrQWNYq2Ws7osrV1EwpRqrck7Ra9FR6QRXWFgmjO-mq4mKFiGyyy3FojiXhBMdaQ6fqRGLMivXdQT3LMnqsMaYYtoNIYvdAoC8RwPBiR0NKaxfUJfT3sFZ41b0w4Wt3IN_F9-N_O0JrtkaCgYKAecSARMSFQGOcNnCHV8my8Ct0YkTgWCC8rQ0zg0171"
                )
                    .body()
            val expected = FirebaseResponse(
                200,
                "Firebase token updated"
            )

            assertEquals(expected, actual)
        }
    }
}
