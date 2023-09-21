package com.example.ecommerce.main.main

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.datastore.DataStoreRepository
import com.example.ecommerce.datastore.DataStoreRepositoryImpl
import com.example.ecommerce.graph.Graph
import com.example.ecommerce.room.cart.CartRepository
import com.example.ecommerce.room.favorite.FavoriteRepository
import com.example.ecommerce.room.notification.NotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow


@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: DataStoreRepositoryImpl,
    private val cartRepository: CartRepository,
    private val favoriteRepository: FavoriteRepository,
    private val notificationRepository: NotificationRepository
) : ViewModel() {

    val cartSize = cartRepository.getAllCarts()
    val favoriteSize = favoriteRepository.getAllFavorite()
    val notificationSize = notificationRepository.getNotification()

    val getBoardingState = repository.getOnBoardingState()

    fun getLoginState() : Flow<Boolean>  {
        return repository.getLoginState()
    }

    fun getProfileName(): Flow<String> {
        return repository.getProfileName()
    }

}