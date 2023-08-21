package com.example.ecommerce.splash

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.datastore.DataStoreRepository
import com.example.ecommerce.graph.Graph
import kotlinx.coroutines.launch
import javax.inject.Inject

class SplashViewModel @Inject constructor(
    private val repository: DataStoreRepository
) : ViewModel() {

    //used for splash screen to decided when should close splash screen
    private val _isLoading: MutableState<Boolean> = mutableStateOf(true)
    val isLoading: State<Boolean> = _isLoading

    //set destination when app is open
    private val _startDestination: MutableState<String> = mutableStateOf(Graph.onBoarding.route)
    val startDestination: State<String> = _startDestination

    //everytime splash screen open get boarding state
    init {
        viewModelScope.launch {
            repository.getOnBoardingState().collect { complete ->
                if (complete) {
                    //if complete is true than go to authentication page
                    _startDestination.value = Graph.Authentication.route
                } else {
                    //if complete is false than go to go to onboarding page
                    _startDestination.value = Graph.onBoarding.route
                }
            }
            //set loading is false
            _isLoading.value = false
        }
    }
}