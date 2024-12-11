package com.example.ecommerce.main.payment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.api.response.BaseResponse
import com.example.core.api.response.PaymentResponse
import com.example.ecommerce.R
import com.example.ecommerce.repository.PaymentRepository
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val paymentRepository: PaymentRepository
) : ViewModel() {

    private val TAG = "PaymentViewModel"

    private val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig
    private val configSettings = remoteConfigSettings {
        minimumFetchIntervalInSeconds = 3600
    }

    private val _paymentResult: MutableLiveData<BaseResponse<PaymentResponse>> = MutableLiveData()
    val paymentResult: LiveData<BaseResponse<PaymentResponse>> get() = _paymentResult

    init {
        remoteConfig()
    }

    private fun remoteConfig() {
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)

        _paymentResult.value = BaseResponse.Loading()
        viewModelScope.launch {
            remoteConfig.fetchAndActivate().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val value = remoteConfig.getString("payment")
                    val json = Gson().fromJson(value, PaymentResponse::class.java)
                    Log.d("PaymentJson", value)
                    Log.d("PaymentJson", json.toString())
                    if (json.data.isNotEmpty()) {
                        _paymentResult.value = BaseResponse.Success(json)
                    } else {
                        _paymentResult.value = BaseResponse.Error("Task is not success")
                    }
                } else {
                    _paymentResult.value = BaseResponse.Error("Task is not success")
                }
            }
        }
    }
}
