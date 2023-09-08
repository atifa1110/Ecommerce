package com.example.ecommerce.main.wishlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.main.cart.ShowCartUiState
import com.example.ecommerce.room.cart.Cart
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
    private val favoriteRepository: FavoriteRepository
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
        }
    }
}

data class ShowFavoriteUiState(
    val favoriteList : Flow<List<Favorite>> = emptyFlow(),
    val isError : Boolean = false,
    val isLoading: Boolean = false,
    val message : String? = null,
)