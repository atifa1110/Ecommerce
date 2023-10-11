package com.example.core.firebase

import com.example.core.api.method.FirebaseService
import com.example.core.api.response.FirebaseResponse
import retrofit2.Response

class MessagingRepository(
    private val api: FirebaseService
) {
    suspend fun firebaseToken(token: String): Response<FirebaseResponse> {
        return api.firebaseToken(token)
    }
}