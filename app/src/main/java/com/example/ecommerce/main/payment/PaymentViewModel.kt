package com.example.ecommerce.main.payment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.api.repository.PaymentRepository
import com.example.ecommerce.api.response.BaseResponse
import com.example.ecommerce.api.response.PaymentResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val paymentRepository: PaymentRepository
) : ViewModel() {

    private val _paymentResult: MutableLiveData<BaseResponse<PaymentResponse>> = MutableLiveData()
    val paymentResult: LiveData<BaseResponse<PaymentResponse>> get() = _paymentResult

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