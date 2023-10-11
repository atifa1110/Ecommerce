package com.example.ecommerce.boarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.firebase.AnalyticsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val repository: com.example.core.datastore.DataStoreRepository,
    private val analyticsRepository: AnalyticsRepository
) : ViewModel() {

    // to save boarding state is complete or not
    fun saveOnBoardingState(complete: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveOnBoardingState(complete = complete)
        }
    }

    fun buttonAnalytics(buttonName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            analyticsRepository.buttonClick(buttonName)
        }
    }
}
