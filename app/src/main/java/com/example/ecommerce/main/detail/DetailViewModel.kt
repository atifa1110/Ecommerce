package com.example.ecommerce.main.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.api.model.ProductDetail
import com.example.ecommerce.api.model.ProductVariant
import com.example.ecommerce.api.repository.ProductRepository
import com.example.ecommerce.api.response.BaseResponse
import com.example.ecommerce.api.response.DetailResponse
import com.example.ecommerce.room.cart.Cart
import com.example.ecommerce.room.cart.CartRepository
import com.example.ecommerce.room.favorite.FavoriteRepository
import com.example.ecommerce.util.Constant
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
class DetailViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val cartRepository: CartRepository,
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {

    private val _detailResult: MutableLiveData<BaseResponse<DetailResponse>> = MutableLiveData()
    val detailResult: LiveData<BaseResponse<DetailResponse>> get() = _detailResult

    fun getProductDetail(id:String){
        _detailResult.value = BaseResponse.Loading()
        viewModelScope.launch {
            try {
                val response = productRepository.getProductDetail(id)
                if (response.code() == 200 && response.isSuccessful) {
                    _detailResult.value = BaseResponse.Success(response.body())
                } else{
                    val jsonObj= JSONObject(response.errorBody()!!.string())
                    val message = jsonObj.getString("message")
                    _detailResult.value = BaseResponse.Error(message)
                }
            } catch (ex: Exception) {
                _detailResult.value = BaseResponse.Error(ex.message)
            }
        }
    }

    private val _uiState = MutableStateFlow(AddProductsUiState())
    val uiState: StateFlow<AddProductsUiState> = _uiState.asStateFlow()

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite : StateFlow<Boolean> = _isFavorite.asStateFlow()

    fun getFavoriteById(id:String){
        viewModelScope.launch(Dispatchers.IO) {
            val result  = favoriteRepository.getFavoriteById(id)
            if(result.isNotEmpty()){
                _isFavorite.value = true
            }
        }
    }

    fun setUnFavorite(){
        _isFavorite.value = false
    }

    fun deleteFavoriteById(id:String){
        viewModelScope.launch(Dispatchers.IO) {
            favoriteRepository.deleteFavoriteById(id)
            _uiState.update {
                it.copy(
                    message = "Produk berhasil dihapus dari favorit"
                )
            }
        }
    }

    fun addProductsToCart(productDetail: ProductDetail, productVariant: ProductVariant) {
        viewModelScope.launch {
            cartRepository.addProductsToCart(productDetail,productVariant)
            _uiState.update {
                it.copy(
                    isSaved = true,
                    message = "Produk berhasil ditambahkan ke keranjang"
                )
            }
        }
    }

    fun addFavoriteToCart(productDetail: ProductDetail, productVariant: ProductVariant) {
        viewModelScope.launch {
            favoriteRepository.addProductsToFavorite(productDetail,productVariant)
            _uiState.update {
                it.copy(
                    isSaved = true,
                    message = "Produk berhasil ditambahkan ke favorit"
                )
            }
            _isFavorite.value = true
        }
    }

    fun setProductDataVariant(variantName: String , variantPrice : Int){
        _uiState.update {
            it.copy(
                productVariant = ProductVariant(variantName,variantPrice)
            )
        }
    }
}

data class AddProductsUiState(
    val cartLocal: Cart = Cart(""),
    val productVariant: ProductVariant = ProductVariant("",0),
    val isCompleted: Boolean = false,
    val isLoading: Boolean = false,
    val isSaved: Boolean = false,
    val message : String? = null
)
