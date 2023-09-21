package com.example.ecommerce.main.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.api.response.BaseResponse
import com.example.ecommerce.main.cart.ShowCartUiState
import com.example.ecommerce.room.cart.Cart
import com.example.ecommerce.room.notification.Notification
import com.example.ecommerce.room.notification.NotificationRepository
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
class NotificationViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ShowNotificationUiState())
    val uiState: StateFlow<ShowNotificationUiState> = _uiState.asStateFlow()

    init {
        getAllNotification()
    }
    private fun getAllNotification(){
        viewModelScope.launch {
            try{
                _uiState.value = ShowNotificationUiState(isLoading = true)
                val result = notificationRepository.getNotification()

                _uiState.update {
                    it.copy(notificationList = result, isError = false)
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

    fun updateReadNotification(id:Int, read:Boolean){
        viewModelScope.launch {
            try {
                val result = notificationRepository.updateReadNotification(id, read)
                _uiState.update {
                    it.copy(
                        message = "Berhasil"
                    )
                }
            }catch (error: Exception){
                _uiState.update {
                    it.copy(
                        message = error.message.toString()
                    )
                }
            }
        }
    }
}

data class ShowNotificationUiState(
    val notificationList : Flow<List<Notification>> = emptyFlow(),
    val isError : Boolean = false,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val message : String = ""
)
