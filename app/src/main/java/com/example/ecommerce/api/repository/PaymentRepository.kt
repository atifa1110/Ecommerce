package com.example.ecommerce.api.repository

import com.example.ecommerce.api.method.PaymentService
import com.example.ecommerce.api.request.FulfillmentRequest
import com.example.ecommerce.api.response.FulfillmentResponse
import com.example.ecommerce.api.response.PaymentResponse
import com.example.ecommerce.api.response.TransactionResponse
import retrofit2.Response
import javax.inject.Inject

class PaymentRepository @Inject constructor(
    private val service: PaymentService,
) {
    suspend fun payment(): Response<PaymentResponse> {
        return service.payment()
    }

    suspend fun fulfillment(fulfillmentRequest: FulfillmentRequest): Response<FulfillmentResponse> {
        return service.fulfillment(fulfillmentRequest)
    }

    suspend fun transaction(): Response<TransactionResponse> {
       return service.transaction()
    }


}