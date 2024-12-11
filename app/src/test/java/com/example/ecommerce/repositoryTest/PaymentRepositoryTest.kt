package com.example.ecommerce.repositoryTest

import com.example.core.api.method.PaymentService
import com.example.core.api.model.Fulfillment
import com.example.core.api.model.Rating
import com.example.core.api.model.Transaction
import com.example.core.api.request.FulfillmentRequest
import com.example.core.api.response.FulfillmentResponse
import com.example.core.api.response.RatingResponse
import com.example.core.api.response.TransactionResponse
import com.example.core.room.cart.CartItem
import com.example.ecommerce.repository.PaymentRepository
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import retrofit2.Response

class PaymentRepositoryTest {

    private lateinit var paymentService: PaymentService
    private lateinit var paymentRepository: PaymentRepository

    @Before
    fun setup() {
        paymentService = mock()
        paymentRepository = PaymentRepository(paymentService)
    }

    @Test
    fun `test fulfillment repository`() {
        runBlocking {
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

            whenever(
                paymentService.fulfillment(
                    FulfillmentRequest(
                        "",
                        listOf(
                            CartItem(
                                "c0aecf56-911c-4d4e-a5a2-f2ecd20f86be",
                                "RAM 16GB",
                                1
                            )
                        )
                    )
                )
            ).thenReturn(Response.success(expected))

            val result = paymentRepository.fulfillment(
                FulfillmentRequest(
                    "",
                    listOf(
                        CartItem(
                            "c0aecf56-911c-4d4e-a5a2-f2ecd20f86be",
                            "RAM 16GB",
                            1
                        )
                    )
                )
            ).body()

            assertEquals(expected, result)
        }
    }

    @Test
    fun `test transaction repository`() {
        runBlocking {
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

            whenever(
                paymentService.transaction()
            ).thenReturn(Response.success(expected))

            val result = paymentRepository.transaction().body()

            assertEquals(expected, result)
        }
    }

    @Test
    fun `test rating repository`() {
        runBlocking {
            val expected = RatingResponse(
                200,
                "Fulfillment rating and review success"
            )

            whenever(
                paymentService.rating(
                    Rating("1", 4, "review")
                )
            ).thenReturn(Response.success(expected))

            val result = paymentRepository.rating(
                Rating("1", 4, "review")
            ).body()

            assertEquals(expected, result)
        }
    }
}
