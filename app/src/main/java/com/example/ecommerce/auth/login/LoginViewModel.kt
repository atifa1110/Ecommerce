package com.example.ecommerce.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.api.request.AuthRequest
import com.example.ecommerce.api.response.BaseResponse
import com.example.ecommerce.api.response.LoginResponse
import com.example.ecommerce.api.repository.AuthRepository
import com.example.ecommerce.api.response.FirebaseResponse
import com.example.ecommerce.datastore.DataStoreRepository
import com.example.ecommerce.firebase.FirebaseMessagingRepository
import com.example.ecommerce.util.Constant
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.json.JSONObject
import javax.inject.Inject


@HiltViewModel
class LoginViewModel@Inject constructor(
    private val loginRepository: AuthRepository,
    private val repository: DataStoreRepository,
    private val firebaseMessagingRepository: FirebaseMessagingRepository
):ViewModel() {

    private val _loginResult: MutableLiveData<BaseResponse<LoginResponse>> = MutableLiveData()
    val loginResult: LiveData<BaseResponse<LoginResponse>> get() = _loginResult

    fun loginUser(api: String, loginRequest: AuthRequest) {
        _loginResult.value = BaseResponse.Loading()
        viewModelScope.launch{
            try {
                val response = loginRepository.loginUser(api, loginRequest)
                if (response.code() == 200 && response.isSuccessful) {
                    _loginResult.value = BaseResponse.Success(response.body())
                } else{
                    val jsonObj= JSONObject(response.errorBody()!!.string())
                    val message = jsonObj.getString("message")
                    _loginResult.value = BaseResponse.Error(message)
                }
            } catch (ex: Exception) {
                _loginResult.value = BaseResponse.Error(ex.message)
            }
        }
    }

    fun saveLoginState(complete: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveHasLoginState(complete = complete)
        }
    }

    fun saveAccessToken(token:String){
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveAccessToken(token = token)
        }
    }

    private val _fcmToken = MutableStateFlow<String?>(null)
    val fcmToken: StateFlow<String?> = _fcmToken

    init{
        refreshFcmToken()
    }

    private val _tokenResult: MutableLiveData<BaseResponse<FirebaseResponse>> = MutableLiveData()
    val tokenResult: LiveData<BaseResponse<FirebaseResponse>> get() = _tokenResult

    fun token(){
        viewModelScope.launch {
            try {
                val response = firebaseMessagingRepository.firebaseToken(Constant.AUTH_TOKEN)
                if (response.code() == 200 && response.isSuccessful) {
                    _tokenResult.value = BaseResponse.Success(response.body())
                }else{
                    val jsonObj= JSONObject(response.errorBody()!!.string())
                    val message = jsonObj.getString("message")
                    _tokenResult.value = BaseResponse.Error(message)
                }
            }catch (error:Exception){
                _tokenResult.value = BaseResponse.Error(error.message.toString())
            }
        }
    }

    private fun refreshFcmToken() {
        viewModelScope.launch {
            try {
                val token = FirebaseMessaging.getInstance().token.await()
                repository.saveTokenMessaging(token)
                _fcmToken.value = token
            } catch (e: Exception) {
                _fcmToken.value = e.message
            }
        }
    }

    fun subscribeFcmTopic() : Boolean{
        return FirebaseMessaging.getInstance().subscribeToTopic("promo").isSuccessful
    }

}