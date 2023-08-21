package com.example.ecommerce.auth.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.api.request.AuthRequest
import com.example.ecommerce.api.response.BaseResponse
import com.example.ecommerce.api.response.LoginResponse
import com.example.ecommerce.api.response.RegisterResponse
import com.example.ecommerce.auth.login.AuthRepository
import kotlinx.coroutines.launch

class RegisterViewModel(private val registerRepository : AuthRepository) : ViewModel() {

    val registerResult: MutableLiveData<BaseResponse<RegisterResponse>> = MutableLiveData()

    fun registerUser(api: String, registerRequest: AuthRequest) {
        registerResult.value = BaseResponse.Loading()
        viewModelScope.launch {
            try {
                val response = registerRepository.registerUser(api, registerRequest)
                if (response.code() == 200) {
                    registerResult.value = BaseResponse.Success(response.body())
                } else if(response.code() == 400){
                    registerResult.value = BaseResponse.Error(response.message())
                } else{
                    registerResult.value = BaseResponse.Error(response.message())
                }
            } catch (ex: Exception) {
                registerResult.value = BaseResponse.Error(ex.message)
            }
        }
    }
}