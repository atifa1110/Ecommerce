package com.example.ecommerce.api.method

import com.example.ecommerce.api.request.AuthRequest
import com.example.ecommerce.api.response.LoginResponse
import com.example.ecommerce.api.response.PaymentResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ProductService {

    @POST("payment")
    suspend fun payment(
        @Header("Authorization") authorization: String,
    ): Response<PaymentResponse>

}