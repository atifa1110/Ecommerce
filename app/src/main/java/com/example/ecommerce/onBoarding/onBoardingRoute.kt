package com.example.ecommerce.onBoarding

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import com.google.accompanist.pager.ExperimentalPagerApi

@OptIn(ExperimentalAnimationApi::class, ExperimentalPagerApi::class)
@ExperimentalMaterial3Api
@Composable
fun onBoardingRoute(
    onJoinClick: () -> Unit,
    onSkipClick: () -> Unit
) {
    onBoardingScreen (onJoinClick, onSkipClick)
}