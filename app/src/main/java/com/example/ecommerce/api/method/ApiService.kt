package com.example.ecommerce.api.method

import com.example.ecommerce.api.request.AuthRequest
import com.example.ecommerce.api.response.LoginResponse
import com.example.ecommerce.api.response.RegisterResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {
    @POST("login")
    suspend fun loginUser(@Header("API_KEY") api : String , @Body loginRequest: AuthRequest): Response<LoginResponse>

    @POST("register")
    suspend fun registerUser(@Header("API_KEY") api : String , @Body registerRequest: AuthRequest): Response<RegisterResponse>

    @POST("refresh")
    suspend fun refresh (@Header("API_KEY") api : String , @Body token : String) : Response<ResponseBody>
}