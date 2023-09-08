package com.example.ecommerce.api.repository

import com.example.ecommerce.api.method.PaymentService
import com.example.ecommerce.api.response.PaymentResponse
import retrofit2.Response
import javax.inject.Inject

class PaymentRepository @Inject constructor(
    private val service: PaymentService,
) {
    suspend fun payment(): Response<PaymentResponse> {
        return service.payment()
    }

}