package com.example.core.api.method

import com.example.core.api.model.Rating
import com.example.core.api.request.FulfillmentRequest
import com.example.core.api.response.FulfillmentResponse
import com.example.core.api.response.RatingResponse
import com.example.core.api.response.TransactionResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface PaymentService {
    @POST("fulfillment")
    suspend fun fulfillment(
        @Body fulfillmentRequest: FulfillmentRequest
    ) : Response<FulfillmentResponse>

    @GET("transaction")
    suspend fun transaction() : Response<TransactionResponse>

    @POST("rating")
    suspend fun rating(
        @Body rating : Rating
    ) : Response<RatingResponse>
}