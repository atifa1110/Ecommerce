package com.example.ecommerce.firebase

import com.example.ecommerce.api.method.FirebaseMessagingApi
import com.example.ecommerce.api.model.Message
import com.example.ecommerce.api.response.FirebaseResponse
import okhttp3.ResponseBody
import retrofit2.Response

class FirebaseMessagingRepository(
    private val api: FirebaseMessagingApi
) {
    suspend fun postNotification(message:Message): Response<ResponseBody> {
        return api.postNotification(message)
    }

    suspend fun firebaseToken(token:String): Response<FirebaseResponse> {
        return api.firebaseToken(token)
    }
}