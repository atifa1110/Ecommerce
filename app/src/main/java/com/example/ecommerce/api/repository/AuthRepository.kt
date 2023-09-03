package com.example.ecommerce.auth.repository

import com.example.ecommerce.api.method.ApiService
import com.example.ecommerce.api.request.AuthRequest
import com.example.ecommerce.api.response.LoginResponse
import com.example.ecommerce.api.response.ProfileResponse
import com.example.ecommerce.api.response.RegisterResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val service: ApiService
){
    suspend fun loginUser(api: String, loginRequest: AuthRequest): Response<LoginResponse> {
        return service.loginUser(api,loginRequest)
    }

    suspend fun registerUser(
        api: String,
        registerRequest: AuthRequest
    ): Response<RegisterResponse> {
        return service.registerUser(api,registerRequest)
    }

    suspend fun profileUser(
        authorization: String,
        userName: String,
        userImage: String
    ): Response<ProfileResponse> {
        return service.profileUser(authorization,userName,userImage)
    }

    suspend fun refresh(api: String, token: String): Response<ResponseBody> {
        return service.refreshToken(api,token)
    }


}