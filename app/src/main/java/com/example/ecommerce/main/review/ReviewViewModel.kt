package com.example.ecommerce.main.review

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.api.repository.ProductRepository
import com.example.ecommerce.api.response.BaseResponse
import com.example.ecommerce.api.response.DetailResponse
import com.example.ecommerce.api.response.ReviewResponse
import com.example.ecommerce.util.Constant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _reviewResult: MutableLiveData<BaseResponse<ReviewResponse>> = MutableLiveData()
    val reviewResult: LiveData<BaseResponse<ReviewResponse>> get() = _reviewResult

    fun getProductReview(id:String){
        _reviewResult.value = BaseResponse.Loading()
        viewModelScope.launch {
            try {
                val response = productRepository.getProductReview(id)
                if (response.code() == 200 && response.isSuccessful) {
                    _reviewResult.value = BaseResponse.Success(response.body())
                } else{
                    val jsonObj= JSONObject(response.errorBody()!!.string())
                    val message = jsonObj.getString("message")
                    _reviewResult.value = BaseResponse.Error(message)
                }
            } catch (ex: Exception) {
                _reviewResult.value = BaseResponse.Error(ex.message)
            }
        }
    }
}
