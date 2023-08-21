package com.example.ecommerce.auth.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.api.request.AuthRequest
import com.example.ecommerce.api.response.BaseResponse
import com.example.ecommerce.api.response.LoginResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


class LoginViewModel():ViewModel() {

    val loginRepository = LoginRepository()
    val loginResult: MutableLiveData<BaseResponse<LoginResponse>> = MutableLiveData()

    fun loginUser(api: String, loginRequest: AuthRequest) {
        loginResult.value = BaseResponse.Loading()
        viewModelScope.launch {
            try {
                val response = loginRepository.loginUser(api, loginRequest)
                if (response.code() == 200) {
                    loginResult.value = BaseResponse.Success(response.body())
                } else {
                    loginResult.value = BaseResponse.Error(response.message())
                }
            } catch (ex: Exception) {
                loginResult.value = BaseResponse.Error(ex.message)
            }
        }
    }
}