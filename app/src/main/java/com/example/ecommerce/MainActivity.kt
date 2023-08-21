package com.example.ecommerce

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.example.ecommerce.auth.login.LoginScreen
import com.example.ecommerce.auth.profile.ProfilePreview
import com.example.ecommerce.auth.profile.ProfileScreen
import com.example.ecommerce.auth.register.RegisterScreen
import com.example.ecommerce.graph.RootNavigationGraph
import com.example.ecommerce.splash.SplashViewModel
import com.example.ecommerce.ui.theme.EcommerceTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@ExperimentalMaterial3Api
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var splashViewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        setContent {
            EcommerceTheme {
                val screen by splashViewModel.startDestination
                val navController = rememberNavController()
                RootNavigationGraph(navController,screen)
                //ProfileScreen()
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