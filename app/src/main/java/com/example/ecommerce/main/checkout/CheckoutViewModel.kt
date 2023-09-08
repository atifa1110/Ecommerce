package com.example.ecommerce.main.checkout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.api.repository.PaymentRepository
import com.example.ecommerce.api.response.BaseResponse
import com.example.ecommerce.api.response.PaymentResponse
import com.example.ecommerce.api.response.SearchResponse
import com.example.ecommerce.room.cart.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
) : ViewModel() {

}