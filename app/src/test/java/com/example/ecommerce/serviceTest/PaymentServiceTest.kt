package com.example.ecommerce.serviceTest

import com.example.core.api.method.PaymentService
import com.example.core.api.model.Fulfillment
import com.example.core.api.model.Rating
import com.example.core.api.model.Transaction
import com.example.core.api.request.FulfillmentRequest
import com.example.core.api.response.FulfillmentResponse
import com.example.core.api.response.RatingResponse
import com.example.core.api.response.TransactionResponse
import com.example.core.room.cart.CartItem
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
class PaymentServiceTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var paymentService: PaymentService

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

        paymentService = retrofit.create(PaymentService::class.java)
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `test fulfillment service`() {
        mockWebServer.enqueueResponse("Fulfillment.json", 200)
        runBlocking {
            val actual = paymentService.fulfillment(
                FulfillmentRequest(
                    "",
                    listOf(
                        CartItem(
                            "b774d021-250a-4c3a-9c58-ab39edb36de5",
                            "RAM 16GB",
                            1
                        )
                    )
                )
            ).body()
            val expected = FulfillmentResponse(
                200,
                "OK",
                Fulfillment(
                    "c0aecf56-911c-4d4e-a5a2-f2ecd20f86be",
                    true,
                    "26 Sep 2023",
                    "15:03",
                    "Bank BCA",
                    13849000
                )
            )

            assertEquals(expected, actual)
        }
    }

    @Test
    fun `test transaction service`() {
        mockWebServer.enqueueResponse("Transaction.json", 200)
        runBlocking {
            val actual = paymentService.transaction().body()
            val expected = TransactionResponse(
                200,
                "OK",
                listOf(
                    Transaction(
                        "c0aecf56-911c-4d4e-a5a2-f2ecd20f86be",
                        true,
                        "26 Sep 2023",
                        "15:03",
                        "Bank BCA",
                        13849000,
                        listOf(
                            CartItem(
                                "b774d021-250a-4c3a-9c58-ab39edb36de5",
                                "RAM 16GB",
                                1
                            )
                        ),
                        "",
                        "",
                        "image",
                        "name"
                    )
                )
            )

            assertEquals(expected, actual)
        }
    }

    @Test
    fun `test rating service`() {
        mockWebServer.enqueueResponse("Rating.json", 200)
        runBlocking {
            val actual = paymentService.rating(
                Rating(
                    "c0aecf56-911c-4d4e-a5a2-f2ecd20f86be",
                    5,
                    "This laptop is good ok"
                )
            ).body()
            val expected = RatingResponse(
                200,
                "Fulfillment rating and review success"
            )

            assertEquals(expected, actual)
        }
    }
}
