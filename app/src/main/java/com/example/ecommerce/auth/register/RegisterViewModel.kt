package com.example.ecommerce.auth.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.api.request.AuthRequest
import com.example.ecommerce.api.response.BaseResponse
import com.example.ecommerce.api.response.RegisterResponse
import com.example.ecommerce.api.repository.AuthRepository
import com.example.ecommerce.datastore.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerRepository : AuthRepository,
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    private val _registerResult: MutableLiveData<BaseResponse<RegisterResponse>> = MutableLiveData()
    val registerResult: LiveData<BaseResponse<RegisterResponse>> get() = _registerResult

    fun registerUser(api: String, registerRequest: AuthRequest) {
        _registerResult.value = BaseResponse.Loading()
        viewModelScope.launch {
            try {
                val response = registerRepository.registerUser(api, registerRequest)
                if (response.code() == 200 && response.isSuccessful) {
                    _registerResult.value = BaseResponse.Success(response.body())
                } else{
                    val jsonObj= JSONObject(response.errorBody()!!.string())
                    val message = jsonObj.getString("message")
                    _registerResult.value = BaseResponse.Error(message)
                }
            } catch (ex: Exception) {
                _registerResult.value = BaseResponse.Error(ex.message)
            }
        }
    }

    fun saveAccessToken(token:String){
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveAccessToken(token)
        }
    }

}