package com.example.ecommerce.api.method

import com.example.ecommerce.api.request.AuthRequest
import com.example.ecommerce.api.request.FulfillmentRequest
import com.example.ecommerce.api.response.FulfillmentResponse
import com.example.ecommerce.api.response.PaymentResponse
import com.example.ecommerce.api.response.TransactionResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface PaymentService {

    @GET("payment")
    suspend fun payment(): Response<PaymentResponse>

    @POST("fulfillment")
    suspend fun fulfillment(
        @Body fulfillmentRequest: FulfillmentRequest
    ) : Response<FulfillmentResponse>

    @GET("transaction")
    suspend fun transaction() : Response<TransactionResponse>
}