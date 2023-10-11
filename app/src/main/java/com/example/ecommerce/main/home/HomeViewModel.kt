package com.example.ecommerce.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.firebase.AnalyticsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: com.example.core.datastore.DataStoreRepositoryImpl,
    private val analyticsRepository: AnalyticsRepository
) : ViewModel() {

    fun saveLoginState(complete: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveHasLoginState(complete = complete)
        }
    }

    fun saveAccessToken(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveAccessToken(token)
        }
    }

    fun saveProfileName(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveProfileName(name)
        }
    }

    fun buttonClicks(buttonName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            analyticsRepository.buttonClick(buttonName)
        }
    }
}
