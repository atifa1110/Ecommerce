package com.example.ecommerce.main.store

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.compose.LazyPagingItems
import com.example.core.api.model.Product
import com.example.core.api.model.ProductFilter
import com.example.core.api.model.RefreshToken
import com.example.core.api.response.BaseResponse
import com.example.core.api.response.RefreshResponse
import com.example.core.api.response.SearchResponse
import com.example.core.datastore.DataStoreRepository
import com.example.core.util.Constant
import com.example.ecommerce.repository.AuthRepository
import com.example.ecommerce.repository.ProductRepository
import com.example.ecommerce.firebase.AnalyticsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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
    private val dataStoreRepository: DataStoreRepository,
    private val analyticsRepository: AnalyticsRepository
) : ViewModel() {

    private val _searchResult: MutableLiveData<BaseResponse<SearchResponse>> = MutableLiveData()
    val searchResult: LiveData<BaseResponse<SearchResponse>> get() = _searchResult

    private val _tokenResult: MutableLiveData<BaseResponse<RefreshResponse>> = MutableLiveData()
    val tokenResult: LiveData<BaseResponse<RefreshResponse>> get() = _tokenResult

    private val _searchData = MutableStateFlow("")
    val searchData = _searchData.asStateFlow()

    fun setSearchText(text: String) {
        _searchData.value = text
    }

    fun searchProductList(query: String) {
        _searchResult.value = BaseResponse.Loading()
        viewModelScope.launch {
            try {
                val response = productRepository.searchProductList(query)
                if (response.code() == 200 && response.isSuccessful) {
                    _searchResult.value = BaseResponse.Success(response.body())
                } else {
                    val jsonObj = JSONObject(response.errorBody()!!.string())
                    val message = jsonObj.getString("message")
                    _searchResult.value = BaseResponse.Error(message)
                }
            } catch (ex: Exception) {
                _searchResult.value = BaseResponse.Error(ex.message)
            }
        }
    }

    fun refreshToken() {
        _tokenResult.value = BaseResponse.Loading()
        viewModelScope.launch {
            try {
                val token = dataStoreRepository.getAccessToken().first()
                val response = authRepository.refresh(Constant.API_KEY, RefreshToken(token))
                if (response.code() == 200 && response.isSuccessful) {
                    _tokenResult.value = BaseResponse.Success(response.body())
                } else {
                    val jsonObj = JSONObject(response.errorBody()!!.string())
                    val message = jsonObj.getString("message")
                    _tokenResult.value = BaseResponse.Error(message)
                }
            } catch (error: Exception) {
                _tokenResult.value = BaseResponse.Error(error.message)
            }
        }
    }

    private val _filter = MutableStateFlow(ProductFilter())
    val filter = _filter.asStateFlow()

    fun searchQuery(search: String?) {
        _filter.update { it.copy(search = search) }
    }

    fun setQuery(brand: String?, lowest: Int?, highest: Int?, sort: String?) {
        _filter.update { it.copy(brand = brand, lowest = lowest, highest = highest, sort = sort) }
    }

    fun resetQuery() {
        _filter.update {
            it.copy(
                search = null,
                brand = null,
                lowest = null,
                highest = null,
                sort = null
            )
        }
    }

    val getProductsFilter = filter.flatMapLatest { filter ->
        productRepository.getProductFilter(
            filter.search,
            filter.brand,
            filter.lowest,
            filter.highest,
            filter.sort
        )
    }.cachedIn(viewModelScope)

    fun searchAnalytics(search: String) {
        viewModelScope.launch(Dispatchers.IO) {
            analyticsRepository.viewSearchResult(search)
        }
    }

    fun buttonAnalytics(buttonName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            analyticsRepository.buttonClick(buttonName)
        }
    }

    fun filterAnalytics(filter: List<String>) {
        viewModelScope.launch(Dispatchers.IO) {
            analyticsRepository.selectItemFilter(filter)
        }
    }

    fun viewListAnalytics(products: LazyPagingItems<Product>) {
        viewModelScope.launch(Dispatchers.IO) {
            analyticsRepository.viewItemList(products)
        }
    }

    fun selectItemProducts(productId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            analyticsRepository.selectItemProduct(productId)
        }
    }
}
