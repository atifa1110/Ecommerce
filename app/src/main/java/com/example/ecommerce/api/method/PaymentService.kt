package com.example.ecommerce.api.method

import com.example.ecommerce.api.response.PaymentResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface PaymentService {

    @GET("payment")
    suspend fun payment(): Response<PaymentResponse>

}