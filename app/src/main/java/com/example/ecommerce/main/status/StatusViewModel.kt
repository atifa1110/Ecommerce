package com.example.ecommerce.main.status

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.api.model.Rating
import com.example.core.api.response.BaseResponse
import com.example.core.api.response.RatingResponse
import com.example.ecommerce.repository.PaymentRepository
import com.example.ecommerce.firebase.AnalyticsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatusViewModel @Inject constructor(
    private val paymentRepository: PaymentRepository,
    private val analyticsRepository: AnalyticsRepository
) : ViewModel() {

    private val _ratingResult: MutableLiveData<BaseResponse<RatingResponse>> = MutableLiveData()
    val ratingResult: LiveData<BaseResponse<RatingResponse>> get() = _ratingResult

    fun ratingStatus(rating: Rating) {
        _ratingResult.value = BaseResponse.Loading()
        viewModelScope.launch {
            val response = paymentRepository.rating(rating)
            if (response.code() == 200 && response.isSuccessful) {
                _ratingResult.value = BaseResponse.Success(response.body())
            } else {
                _ratingResult.value = BaseResponse.Error(response.message())
            }
        }
    }

    fun buttonAnalytics(buttonName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            analyticsRepository.buttonClick(buttonName)
        }
    }
}
