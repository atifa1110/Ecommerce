package com.example.ecommerce.main.cart

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.room.cart.Cart
import com.example.ecommerce.room.cart.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.sample
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository
) : ViewModel() {

    val list = cartRepository.getAllCarts()

    private val _buttonVisible = MutableStateFlow(false)
    val buttonVisible: StateFlow<Boolean> = _buttonVisible.asStateFlow()

    private val _selectedAll = MutableStateFlow(false)
    val selectedAll: StateFlow<Boolean> = _selectedAll.asStateFlow()

    private val _uiState = MutableStateFlow(ShowCartUiState())
    val uiState: StateFlow<ShowCartUiState> = _uiState.asStateFlow()

    init {
        getAllCart()
    }

    fun getCheckedSelected(){
        viewModelScope.launch(Dispatchers.IO) {
            val result = cartRepository.checkSelected()
            _buttonVisible.value = result
        }
    }

    fun getAllSelected(size : Int){
        viewModelScope.launch(Dispatchers.IO) {
           val result = cartRepository.getAllSelected()
            Log.d("CartRepository",result.toString())
            _selectedAll.value = size == result.size
        }
    }

    private fun getAllCart(){
        viewModelScope.launch {
            try{
                _uiState.value = ShowCartUiState(isLoading = true)
                val result = cartRepository.getAllCarts()

                _uiState.update {
                    it.copy(cartList = result, isError = false)
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

    fun deleteCartById(id: String) {
        viewModelScope.launch {
            try {
                cartRepository.deleteById(id)
                _uiState.update {
                    it.copy(
                        isError = false,
                        message = "Deleted Success"
                    )
                }
            } catch (error: Exception) {
                _uiState.update {
                    it.copy(
                        isError = true,
                        message = "Deleted Failed"
                    )
                }
            }
        }
    }

    fun deletedCartBySelected(){
        viewModelScope.launch {
            cartRepository.deletedBySelected()
        }
    }

    fun selectedCart(cart: Cart, checked: Boolean) {
        viewModelScope.launch {
            cartRepository.selected(cart.productId, checked)
        }
    }

    fun selectedAllCart(checked: Boolean){
        viewModelScope.launch {
            _selectedAll.value = checked
            cartRepository.selectedAll(checked)
        }
    }
    fun deleteAllCart() {
        viewModelScope.launch {
            try {
                cartRepository.deleteAllCart()
                _uiState.update {
                    it.copy(
                        cartList = emptyFlow(),
                        isError = false,
                        message = "Deleted Success"
                    )
                }
            } catch (error: Exception) {
                _uiState.update {
                    it.copy(
                        isError = true,
                        message = "Deleted Failed"
                    )
                }
            }
        }
    }

    fun getTotal(){
        viewModelScope.launch {
            val result = cartRepository.getTotal()
            _uiState.update {
                it.copy(
                    total = result
                )
            }
        }
    }

    fun addQuantity(cart: Cart, quantity: Int){
        viewModelScope.launch {
            cartRepository.updateAddQuantity(cart,quantity)
        }
    }
}

data class ShowCartUiState(
    val cartList : Flow<List<Cart>> = emptyFlow(),
    val isError : Boolean = false,
    val isLoading: Boolean = false,
    val message : String? = null,
    val total : Int? = 0
)


