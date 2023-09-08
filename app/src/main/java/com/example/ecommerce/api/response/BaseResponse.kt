package com.example.ecommerce.api.response

sealed class BaseResponse<out T> {
    data class Loading(val nothing: Nothing?=null) : BaseResponse<Nothing>()
    data class Success<T>(val data: T? = null) : BaseResponse<T>()
    data class Error(val msg: String?) : BaseResponse<Nothing>()
}