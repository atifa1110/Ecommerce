package com.example.ecommerce.main

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.datastore.DataStoreRepositoryImpl
import com.example.ecommerce.graph.Graph
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.concurrent.Flow
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: DataStoreRepositoryImpl
) : ViewModel() {

    //set destination when app is open
    private val _startDestination: MutableState<String> = mutableStateOf(Graph.OnBoarding.route)
    val startDestination: State<String> = _startDestination

    //has Logged in
    private val _hasLoggedIn: MutableState<Boolean> = mutableStateOf(false)
    val hasLoggedIn: State<Boolean> = _hasLoggedIn

    private val _hasCompleteOnBoarding: MutableState<Boolean> = mutableStateOf(false)
    val hasCompleteOnBoarding: State<Boolean> = _hasCompleteOnBoarding

    fun getOnBoardingState(){
        viewModelScope.launch {
            repository.getOnBoardingState().collect { complete ->
                if (complete) {
                    //if complete is true than go to authentication page
                    _startDestination.value = Graph.Authentication.route
                }
            }
        }
    }

    suspend fun getLoginState() : Boolean {
        return repository.getLoginState().first()
    }

    suspend fun getBoarding() : Boolean {
        return repository.getOnBoardingState().first()
    }
}