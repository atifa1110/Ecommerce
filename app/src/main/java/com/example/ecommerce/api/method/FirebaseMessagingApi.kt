package com.example.ecommerce.api.method

import com.example.ecommerce.api.model.Message
import com.example.ecommerce.api.response.FirebaseResponse
import com.example.ecommerce.util.Constant
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface FirebaseMessagingApi {

    @POST("messages")
    @Headers("Authorization ${Constant.AUTHORIZATION_MESSAGING}")
    suspend fun postNotification(
        @Body message: Message
    ): Response<ResponseBody>

    @POST("firebase")
    suspend fun firebaseToken(
        @Body token : String
    ) : Response<FirebaseResponse>
}