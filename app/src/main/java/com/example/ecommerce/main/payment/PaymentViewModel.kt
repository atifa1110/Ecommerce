package com.example.ecommerce.main.payment

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.R
import com.example.ecommerce.api.repository.PaymentRepository
import com.example.ecommerce.api.response.BaseResponse
import com.example.ecommerce.api.response.PaymentResponse
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val paymentRepository: PaymentRepository
) : ViewModel() {

    private val TAG = "PaymentViewModel"

    val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig
    val configSettings = remoteConfigSettings {
        minimumFetchIntervalInSeconds = 3600
    }

    private val _paymentResult: MutableLiveData<BaseResponse<PaymentResponse>> = MutableLiveData()
    val paymentResult: LiveData<BaseResponse<PaymentResponse>> get() = _paymentResult

    init {
        remoteConfig()
    }

    private fun remoteConfig(){
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)

        _paymentResult.value = BaseResponse.Loading()
        viewModelScope.launch {
            remoteConfig.fetchAndActivate().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val value = remoteConfig.getString("payment")
                    val json = Gson().toJson(value)
                    if(json.isNotEmpty()){
                       // _paymentResult.value = BaseResponse.Success(json)
                    }else{
                        _paymentResult.value = BaseResponse.Error("Task is not success")
                    }
                    Log.d(TAG, "Config params updated: $json")
                } else {
                    _paymentResult.value = BaseResponse.Error("Task is not success")
                }
            }
        }
    }

    init {
        getPaymentMethod()
    }

    private fun getPaymentMethod(){
        _paymentResult.value = BaseResponse.Loading()
        viewModelScope.launch {
            try {
                val response = paymentRepository.payment()
                if (response.code() == 200 && response.isSuccessful) {
                    Log.d("ResponseBodyPayment", response.body().toString())
                    _paymentResult.value = BaseResponse.Success(response.body())
                } else{
                    val jsonObj= JSONObject(response.errorBody()!!.string())
                    val message = jsonObj.getString("message")
                    _paymentResult.value = BaseResponse.Error(message)
                }
            }catch (error : Exception){
                _paymentResult.value = BaseResponse.Error(error.message)
            }
        }
    }
}