package com.example.ecommerce.auth.login

import com.example.ecommerce.api.method.ApiClient
import com.example.ecommerce.api.method.ApiService
import com.example.ecommerce.api.request.AuthRequest
import com.example.ecommerce.api.response.LoginResponse
import com.example.ecommerce.api.response.RegisterResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject

class AuthRepository @Inject constructor(private val retrofit: Retrofit) : ApiService{

    private val service = retrofit.create(ApiService::class.java)

    override suspend fun loginUser(api: String, loginRequest: AuthRequest): Response<LoginResponse> {
        return service.loginUser(api,loginRequest)
    }

    override suspend fun registerUser(api: String, registerRequest: AuthRequest): Response<RegisterResponse> {
        return service.registerUser(api,registerRequest)
    }

    override suspend fun refresh(api: String, token: String): Response<ResponseBody> {
        return service.refresh(api,token)
    }
}