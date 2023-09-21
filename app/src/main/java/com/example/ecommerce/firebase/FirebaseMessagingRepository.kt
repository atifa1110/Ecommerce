package com.example.ecommerce.firebase

import com.example.ecommerce.api.method.FirebaseMessagingApi
import com.example.ecommerce.api.model.Message
import okhttp3.ResponseBody
import retrofit2.Response

class FirebaseMessagingRepository(
    private val api: FirebaseMessagingApi
) {
    suspend fun postNotification(message:Message): Response<ResponseBody> {
        return api.postNotification(message)
    }
}