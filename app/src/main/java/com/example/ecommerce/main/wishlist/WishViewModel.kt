package com.example.ecommerce.main.wishlist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.api.model.ProductDetail
import com.example.ecommerce.api.model.ProductVariant
import com.example.ecommerce.main.cart.ShowCartUiState
import com.example.ecommerce.room.cart.Cart
import com.example.ecommerce.room.cart.CartRepository
import com.example.ecommerce.room.favorite.Favorite
import com.example.ecommerce.room.favorite.FavoriteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class WishViewModel @Inject constructor(
    private val favoriteRepository: FavoriteRepository,
    private val cartRepository: CartRepository
) : ViewModel(){

    private val _uiState = MutableStateFlow(ShowFavoriteUiState())
    val uiState: StateFlow<ShowFavoriteUiState> = _uiState.asStateFlow()

    init {
        getAllFavorite()
    }

    private fun getAllFavorite(){
        viewModelScope.launch {
            try{
                _uiState.value = ShowFavoriteUiState(isLoading = true)
                val result = favoriteRepository.getAllFavorite()

                _uiState.update {
                    it.copy(favoriteList = result, isError = false)
                }

            }catch (error : Exception){
                _uiState.update {
                    it.copy(
                        isError = true,
                        message = "Your requested data is unavailable"
                    )
                }
            }
        }
    }

    fun deleteFavoriteById(id:String){
        viewModelScope.launch {
            favoriteRepository.deleteFavoriteById(id)
            _uiState.update {
                it.copy(
                    message = "Produk berhasil dihapus dari favorit"
                )
            }
        }
    }

    fun addFavoriteToCart(favorite : Favorite) {
        viewModelScope.launch {
            cartRepository.addFavoriteToCart(favorite)
            _uiState.update {
                it.copy(
                    message = "Produk berhasil ditambahkan ke keranjang"
                )
            }
        }
    }
}

data class ShowFavoriteUiState(
    val favoriteList : Flow<List<Favorite>> = emptyFlow(),
    val isError : Boolean = false,
    val isLoading: Boolean = false,
    val message : String? = null,
)