package com.example.ecommerce

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.example.ecommerce.auth.profile.ProfileScreen
import com.example.ecommerce.graph.Graph
import com.example.ecommerce.graph.Main
import com.example.ecommerce.graph.RootNavigationGraph
import com.example.ecommerce.main.checkout.CheckoutScreen
import com.example.ecommerce.main.detail.DetailScreen
import com.example.ecommerce.main.home.HomeScreen
import com.example.ecommerce.main.main.MainViewModel
import com.example.ecommerce.main.payment.PaymentScreen
import com.example.ecommerce.main.review.ReviewScreen
import com.example.ecommerce.main.status.StatusScreen
import com.example.ecommerce.ui.theme.EcommerceTheme
import com.google.accompanist.pager.ExperimentalPagerApi
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@ExperimentalPagerApi
@ExperimentalMaterialApi
@ExperimentalMaterial3Api
@ExperimentalLayoutApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().setKeepOnScreenCondition {
            !mainViewModel.isLoading.value
        }

        setContent {
            mainViewModel.getOnBoardingState()
            val screen by mainViewModel.startDestination
            val boarding by mainViewModel.getBoardingState.collectAsState(false)
            val screenBoarding = if(boarding) Graph.Authentication.route else Graph.OnBoarding.route

            val login = mainViewModel.getLoginState().collectAsStateWithLifecycle(null)
            val screenLogin = if(login.value == true) Graph.Main.route else screenBoarding

            val profile = mainViewModel.getProfileName().collectAsStateWithLifecycle("")
            val screenProfile = if(profile.value.isNotEmpty()) Main.Home.route else Main.Profile.route

            EcommerceTheme{
                val navController = rememberNavController()
                RootNavigationGraph(navController,screenLogin)
                //ProfileScreen {}
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    EcommerceTheme {

    }
}