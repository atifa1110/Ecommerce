package com.example.ecommerce.main.store

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.ecommerce.api.model.ProductFilter
import com.example.ecommerce.api.model.Product
import com.example.ecommerce.api.repository.AuthRepository
import com.example.ecommerce.api.response.BaseResponse
import com.example.ecommerce.api.response.SearchResponse
import com.example.ecommerce.api.repository.ProductRepository
import com.example.ecommerce.api.response.RegisterResponse
import com.example.ecommerce.datastore.DataStoreRepository
import com.example.ecommerce.util.Constant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class StoreViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val authRepository: AuthRepository,
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    private val _searchResult: MutableLiveData<BaseResponse<SearchResponse>> = MutableLiveData()
    val searchResult: LiveData<BaseResponse<SearchResponse>> get() = _searchResult

    private val _tokenResult: MutableLiveData<BaseResponse<RegisterResponse>> = MutableLiveData()
    val tokenResult: LiveData<BaseResponse<RegisterResponse>> get() = _tokenResult

    private val _searchData = MutableStateFlow("")
    val searchData = _searchData.asStateFlow()

    fun setSearchText (text:String){
        _searchData.value = text
    }

    fun searchProductList(query : String){
        _searchResult.value = BaseResponse.Loading()
        viewModelScope.launch {
            try {
                val response = productRepository.searchProductList(query)
                if (response.code() == 200 && response.isSuccessful) {
                    _searchResult.value = BaseResponse.Success(response.body())
                } else{
                    val jsonObj= JSONObject(response.errorBody()!!.string())
                    val message = jsonObj.getString("message")
                    _searchResult.value = BaseResponse.Error(message)
                }
            } catch (ex: Exception) {
                _searchResult.value = BaseResponse.Error(ex.message)
            }
        }
    }

    fun refreshToken(){
        _tokenResult.value = BaseResponse.Loading()
        viewModelScope.launch {
            try{
                val token = dataStoreRepository.getAccessToken().first().toString()
                Log.d("TokenForRefresh",token)
                val response = authRepository.refresh(Constant.API_KEY,token)
                if (response.code() == 200 && response.isSuccessful) {
                    _tokenResult.value = BaseResponse.Success(response.body())
                }else{
                    val jsonObj= JSONObject(response.errorBody()!!.string())
                    val message = jsonObj.getString("message")
                    _tokenResult.value = BaseResponse.Error(message)
                }
            }catch (error:Exception){
                _tokenResult.value = BaseResponse.Error(error.message)
            }
        }
    }

    private val _filter = MutableStateFlow(ProductFilter())
    val filter = _filter.asStateFlow()

    fun searchQuery(search: String?){
        _filter.update { it.copy(search = search) }
    }

    fun setQuery(brand: String?,lowest: Int?,highest: Int?,sort: String?){
        _filter.update { it.copy(brand=brand, lowest = lowest, highest = highest, sort = sort) }
    }

    fun resetQuery(){
        _filter.update { it.copy(search=null,brand=null, lowest = null, highest = null, sort = null) }
    }

    val getProductsFilter = filter.flatMapLatest { filter ->
        productRepository.getProductFilter(
        filter.search,
        filter.brand,
        filter.lowest,
        filter.highest,
        filter.sort)
    }.cachedIn(viewModelScope)
}