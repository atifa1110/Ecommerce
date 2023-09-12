package com.example.ecommerce.main.transaction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.api.repository.PaymentRepository
import com.example.ecommerce.api.repository.ProductRepository
import com.example.ecommerce.api.response.BaseResponse
import com.example.ecommerce.api.response.SearchResponse
import com.example.ecommerce.api.response.TransactionResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val paymentRepository: PaymentRepository
) : ViewModel() {

    private val _transactionResult: MutableLiveData<BaseResponse<TransactionResponse>> = MutableLiveData()
    val transactionResult: LiveData<BaseResponse<TransactionResponse>> get() = _transactionResult

    init {
        getAllTransaction()
    }

    private fun getAllTransaction(){
        _transactionResult.value = BaseResponse.Loading()
        viewModelScope.launch {
            try{
                val response = paymentRepository.transaction()
                if (response.code() == 200 && response.isSuccessful) {
                    _transactionResult.value = BaseResponse.Success(response.body())
                } else{
                    val jsonObj= JSONObject(response.errorBody()!!.string())
                    val message = jsonObj.getString("message")
                    _transactionResult.value = BaseResponse.Error(message)
                }
            } catch (ex: Exception) {
                _transactionResult.value = BaseResponse.Error(ex.message)
            }
        }
    }
}