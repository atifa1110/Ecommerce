package com.example.ecommerce.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.datastore.DataStoreRepository
import com.example.ecommerce.datastore.DataStoreRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: DataStoreRepositoryImpl
): ViewModel() {

    fun saveLoginState(complete: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveHasLoginState(complete = complete)
        }
    }

    fun saveAccessToken(token:String){
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveAccessToken(token)
        }
    }

    fun getProfileName(): Flow<String> {
        return repository.getProfileName()
    }
}