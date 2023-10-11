package com.example.core.api.method

import com.example.core.api.response.FirebaseResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface FirebaseService {
    @POST("firebase")
    suspend fun firebaseToken(
        @Body token: String
    ): Response<FirebaseResponse>
}