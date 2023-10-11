package com.example.ecommerce.main.main

import androidx.lifecycle.ViewModel
import com.example.core.datastore.DataStoreRepositoryImpl
import com.example.core.room.notification.NotificationRepository
import com.example.ecommerce.room.cart.CartRepository
import com.example.ecommerce.room.favorite.FavoriteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

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
    val getLoginState: Flow<Boolean> = repository.getLoginState()

    fun getLoginState(): Boolean {
        var isLogin = false
        runBlocking {
            isLogin = try {
                repository.getLoginState().first()
            } catch (ex: Exception) {
                false
            }
        }
        return isLogin
    }

    fun getBoardingState(): Boolean {
        var isBoarding = false
        runBlocking {
            isBoarding = try {
                repository.getOnBoardingState().first()
            } catch (ex: Exception) {
                false
            }
        }
        return isBoarding
    }

    fun getProfileName(): String {
        var isProfile = ""
        runBlocking {
            isProfile = try {
                repository.getProfileName().first()
            } catch (ex: Exception) {
                "Error"
            }
        }
        return isProfile
    }
}
