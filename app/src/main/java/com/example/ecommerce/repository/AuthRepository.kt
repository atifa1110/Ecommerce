package com.example.ecommerce.test

import com.example.core.api.method.ApiService
import com.example.core.api.model.RefreshToken
import com.example.core.api.request.AuthRequest
import com.example.core.api.response.LoginResponse
import com.example.core.api.response.ProfileResponse
import com.example.core.api.response.RefreshResponse
import com.example.core.api.response.RegisterResponse
import okhttp3.MultipartBody
import retrofit2.Response
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val service: ApiService
) {
    suspend fun loginUser(
        api: String,
        loginRequest: AuthRequest
    ): Response<LoginResponse> {
        return service.loginUser(api, loginRequest)
    }

    suspend fun registerUser(
        api: String,
        registerRequest: AuthRequest
    ): Response<RegisterResponse> {
        return service.registerUser(api, registerRequest)
    }

    suspend fun profileUser(
        userImage: MultipartBody.Part,
        userName: MultipartBody.Part
    ): Response<ProfileResponse> {
        return service.profileUser(userImage, userName)
    }

    suspend fun refresh(
        api: String,
        token: RefreshToken
    ): Response<RefreshResponse> {
        return service.refreshToken(api, token)
    }
}
