package com.example.ecommerce.boarding

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi

@OptIn(ExperimentalAnimationApi::class, ExperimentalPagerApi::class)
@ExperimentalMaterial3Api
@Composable
fun onBoardingRoute(
    onNavigateToRegister: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val onBoardingViewModel: OnBoardingViewModel = hiltViewModel()

    onBoardingScreen(
        // set navigation to register and save boarding state to complete
        onNavigateToRegister = {
            onNavigateToRegister()
            onBoardingViewModel.saveOnBoardingState(complete = true)
        },
        // set navigation to login and save boarding state to complete
        onNavigateToLogin = {
            onNavigateToLogin()
            onBoardingViewModel.saveOnBoardingState(complete = true)
        }
    )
}
