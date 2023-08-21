package com.example.ecommerce.auth.login

import androidx.lifecycle.MutableLiveData
import com.example.ecommerce.api.method.ApiClient
import com.example.ecommerce.api.method.ApiService
import com.example.ecommerce.api.request.AuthRequest
import com.example.ecommerce.api.response.LoginResponse
import com.example.ecommerce.api.response.RegisterResponse
import retrofit2.Response

class LoginRepository() : ApiService{
    override suspend fun loginUser(api: String, loginRequest: AuthRequest): Response<LoginResponse> {
       return ApiClient.getApiService().loginUser(api,loginRequest)
    }

    override suspend fun registerUser(api: String, registerRequest: AuthRequest): Response<RegisterResponse> {
        return ApiClient.getApiService().registerUser(api,registerRequest)
    }
}