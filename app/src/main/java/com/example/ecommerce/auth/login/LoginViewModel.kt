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


@HiltViewModel
class LoginViewModel@Inject constructor(private val loginRepository: AuthRepository):ViewModel() {

    val loginResult: MutableLiveData<BaseResponse<String>> = MutableLiveData()

    fun loginUser(api: String, loginRequest: AuthRequest) {
        loginResult.value = BaseResponse.Loading()
        viewModelScope.launch {
            try {
                val response = loginRepository.loginUser(api, loginRequest)
                if (response.code() == 200) {
                    loginResult.value = BaseResponse.Success(response.body().toString())
                } else if(response.code() == 400){
                    loginResult.value = BaseResponse.Error(response.message())
                } else{
                    loginResult.value = BaseResponse.Error(response.message())
                }
            } catch (ex: Exception) {
                loginResult.value = BaseResponse.Error(ex.message)
            }
        }
    }
}