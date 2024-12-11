package com.example.ecommerce.auth.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.api.request.AuthRequest
import com.example.core.api.response.BaseResponse
import com.example.core.api.response.RegisterResponse
import com.example.core.firebase.MessagingRepository
import com.example.ecommerce.repository.AuthRepository
import com.example.ecommerce.firebase.AnalyticsRepository
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerRepository: AuthRepository,
    private val dataStoreRepository: com.example.core.datastore.DataStoreRepository,
    private val messagingRepository: MessagingRepository,
    private val analyticsRepository: AnalyticsRepository,
) : ViewModel() {

    private val _registerResult: MutableLiveData<BaseResponse<RegisterResponse>> = MutableLiveData()
    val registerResult: LiveData<BaseResponse<RegisterResponse>> get() = _registerResult

    fun registerUser(api: String, registerRequest: AuthRequest) {
        _registerResult.postValue(BaseResponse.Loading())
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = registerRepository.registerUser(api, registerRequest)
                if (response.code() == 200 && response.isSuccessful) {
                    _registerResult.postValue(BaseResponse.Success(response.body()))
                } else {
                    val jsonObj = JSONObject(response.errorBody()!!.string())
                    val message = jsonObj.getString("message")
                    _registerResult.postValue(BaseResponse.Error(message))
                }
            } catch (ex: Exception) {
                _registerResult.postValue(BaseResponse.Error(ex.message))
            }
        }
    }

    fun registerAnalytics(email: String) {
        viewModelScope.launch(Dispatchers.IO) {
            analyticsRepository.registerAnalytics(email)
        }
    }

    fun saveLoginState(complete: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveHasLoginState(complete = complete)
        }
    }

    fun buttonAnalytics(buttonName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            analyticsRepository.buttonClick(buttonName)
        }
    }

    fun saveAccessToken(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveAccessToken(token)
        }
    }

    private val _fcmToken = MutableStateFlow<String?>(null)
    val fcmToken: StateFlow<String?> = _fcmToken

    init {
        refreshFcmToken()
    }

    private fun refreshFcmToken() {
        viewModelScope.launch {
            try {
                val token = FirebaseMessaging.getInstance().token.await()
                _fcmToken.value = token
            } catch (e: Exception) {
                _fcmToken.value = e.message
            }
        }
    }
}
