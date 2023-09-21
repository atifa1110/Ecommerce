package com.example.ecommerce

import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.example.ecommerce.graph.Graph
import com.example.ecommerce.graph.Main
import com.example.ecommerce.graph.RootNavigationGraph
import com.example.ecommerce.main.main.MainViewModel
import com.example.ecommerce.ui.theme.EcommerceTheme
import com.example.ecommerce.component.PermissionDialog
import com.example.ecommerce.component.RationaleDialog
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val mainViewModel by viewModels<MainViewModel>()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        setContent {
            val boarding by mainViewModel.getBoardingState.collectAsState(false)
            val screenBoarding = if(boarding) Graph.Authentication.route else Graph.OnBoarding.route

            val login = mainViewModel.getLoginState().collectAsStateWithLifecycle(null)
            val screenLogin = if(login.value == true) Graph.Main.route else screenBoarding

            val profile = mainViewModel.getProfileName().collectAsStateWithLifecycle("")
            val screenProfile = if(profile.value.isNotEmpty()) Main.Home.route else Main.Profile.route


            EcommerceTheme{
                val navController = rememberNavController()
                RootNavigationGraph(navController,screenLogin,screenProfile)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestNotificationPermissionDialog() {
    val permissionState = rememberPermissionState(permission = android.Manifest.permission.POST_NOTIFICATIONS)

    if (!permissionState.status.isGranted) {
        if (permissionState.status.shouldShowRationale) RationaleDialog()
        else PermissionDialog { permissionState.launchPermissionRequest() }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    EcommerceTheme {

    }
}