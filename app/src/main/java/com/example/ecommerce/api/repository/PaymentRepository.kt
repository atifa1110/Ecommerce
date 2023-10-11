package com.example.ecommerce.api.repository

import com.example.core.api.method.PaymentService
import com.example.core.api.model.Rating
import com.example.core.api.request.FulfillmentRequest
import com.example.core.api.response.FulfillmentResponse
import com.example.core.api.response.RatingResponse
import com.example.core.api.response.TransactionResponse
import retrofit2.Response
import javax.inject.Inject

class PaymentRepository @Inject constructor(
    private val service: PaymentService
) {
    suspend fun fulfillment(fulfillmentRequest: FulfillmentRequest): Response<FulfillmentResponse> {
        return service.fulfillment(fulfillmentRequest)
    }

    suspend fun transaction(): Response<TransactionResponse> {
        return service.transaction()
    }

    suspend fun rating(
        rating: Rating
    ): Response<RatingResponse> {
        return service.rating(rating)
    }
}
