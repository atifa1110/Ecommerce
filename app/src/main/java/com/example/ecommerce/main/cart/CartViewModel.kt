package com.example.ecommerce.main.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.room.cart.Cart
import com.example.ecommerce.firebase.AnalyticsRepository
import com.example.ecommerce.room.cart.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository,
    private val analyticsRepository: AnalyticsRepository
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
        getAllSelected()
    }

    fun getCheckedSelected() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = cartRepository.checkSelected()
            _buttonVisible.value = result
        }
    }

    fun checkSelected(size: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = cartRepository.getAllSelected().first()
            _selectedAll.value = size == result.size
        }
    }

    private fun getAllSelected() {
        viewModelScope.launch {
            val result = cartRepository.getAllSelected()
            _uiState.update {
                it.copy(cartSelected = result)
            }
        }
    }

    private fun getAllCart() {
        viewModelScope.launch {
            try {
                _uiState.value = ShowCartUiState(isLoading = true)
                val result = cartRepository.getAllCarts()

                _uiState.update {
                    it.copy(cartList = result, isError = false)
                }
            } catch (error: Exception) {
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

    fun deletedCartBySelected() {
        viewModelScope.launch {
            cartRepository.deletedBySelected()
        }
    }

    fun selectedCart(cart: Cart, checked: Boolean) {
        viewModelScope.launch {
            cartRepository.updateChecked(cart.productId, checked)
        }
    }

    fun selectedAllCart(checked: Boolean) {
        viewModelScope.launch {
            _selectedAll.value = checked
            cartRepository.updateCheckedAll(checked)
        }
    }

    fun getTotal() {
        viewModelScope.launch {
            val result = cartRepository.getTotal()
            _uiState.update {
                it.copy(
                    total = result
                )
            }
        }
    }

    fun addQuantity(cart: Cart, quantity: Int) {
        viewModelScope.launch {
            cartRepository.updateQuantity(cart, quantity)
        }
    }

    fun removeCartAnalytics(productId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            analyticsRepository.removeFromCart(productId)
        }
    }

    fun viewCartAnalytics(cart: List<Cart>) {
        viewModelScope.launch(Dispatchers.IO) {
            analyticsRepository.viewCart(cart)
        }
    }

    fun buttonAnalytics(buttonName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            analyticsRepository.buttonClick(buttonName)
        }
    }
}

data class ShowCartUiState(
    val cartList: Flow<List<Cart>> = emptyFlow(),
    val cartSelected: Flow<List<Cart>> = emptyFlow(),
    val isError: Boolean = false,
    val isLoading: Boolean = false,
    val message: String? = null,
    val total: Int? = 0
)
