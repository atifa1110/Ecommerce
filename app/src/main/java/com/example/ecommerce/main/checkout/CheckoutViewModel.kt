package com.example.ecommerce.main.checkout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.api.model.Fulfillment
import com.example.core.api.request.FulfillmentRequest
import com.example.core.api.response.BaseResponse
import com.example.core.api.response.FulfillmentResponse
import com.example.core.room.cart.Cart
import com.example.ecommerce.api.repository.PaymentRepository
import com.example.ecommerce.firebase.AnalyticsRepository
import com.example.ecommerce.room.cart.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val cartRepository: CartRepository,
    private val paymentRepository: PaymentRepository,
    private val analyticsRepository: AnalyticsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CheckOutUiState())
    val uiState: StateFlow<CheckOutUiState> = _uiState.asStateFlow()

    private val _checkOutResult: MutableLiveData<BaseResponse<FulfillmentResponse>> =
        MutableLiveData()
    val checkOutResult: LiveData<BaseResponse<FulfillmentResponse>> get() = _checkOutResult

    init {
        getTotalCheckOut()
    }

    private fun getTotalCheckOut() {
        viewModelScope.launch {
            val result = cartRepository.getTotal()
            _uiState.update {
                it.copy(
                    total = result
                )
            }
        }
    }

    fun addQuantity(cart: Cart, quantity: Int) {
        viewModelScope.launch {
            cartRepository.updateQuantity(cart, quantity)
        }
    }

    fun fulfillment(fulfillmentRequest: FulfillmentRequest) {
        _checkOutResult.value = BaseResponse.Loading()
        viewModelScope.launch {
            try {
                val response = paymentRepository.fulfillment(fulfillmentRequest)
                if (response.code() == 200 && response.isSuccessful) {
                    _checkOutResult.value = BaseResponse.Success(response.body())
                } else {
                    val jsonObj = JSONObject(response.errorBody()!!.string())
                    val message = jsonObj.getString("message")
                    _checkOutResult.value = BaseResponse.Error(message)
                }
            } catch (ex: Exception) {
                _checkOutResult.value = BaseResponse.Error(ex.message.toString())
            }
        }
    }

    fun beginToCheckOutAnalytics(checkOut: List<Cart>) {
        viewModelScope.launch(Dispatchers.IO) {
            analyticsRepository.beginCheckOut(checkOut)
        }
    }

    fun addPaymentInfo(payment: String) {
        viewModelScope.launch(Dispatchers.IO) {
            analyticsRepository.addPaymentInfo(payment)
        }
    }

    fun purchase(checkout: Fulfillment?) {
        viewModelScope.launch(Dispatchers.IO) {
            analyticsRepository.purchase(checkout)
        }
    }

    fun buttonAnalytics(buttonName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            analyticsRepository.buttonClick(buttonName)
        }
    }
}

data class CheckOutUiState(
    val total: Int? = 0
)
