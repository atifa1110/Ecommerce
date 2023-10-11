package com.example.ecommerce

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.example.ecommerce.component.PermissionDialog
import com.example.ecommerce.component.RationaleDialog
import com.example.ecommerce.graph.RootNavigationGraph
import com.example.ecommerce.main.main.MainViewModel
import com.example.ecommerce.ui.theme.EcommerceTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class, ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // WindowCompat.setDecorFitsSystemWindows(window,false)
        installSplashScreen()

        setContent {
            RequestNotificationPermissionDialog()
            EcommerceTheme {
                val navController = rememberNavController()
                val windowSizeClass = calculateWindowSizeClass(this)
                RootNavigationGraph(navController, windowSizeClass)
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestNotificationPermissionDialog() {
    val permissionState =
        rememberPermissionState(permission = android.Manifest.permission.POST_NOTIFICATIONS)

    if (!permissionState.status.isGranted) {
        if (permissionState.status.shouldShowRationale) {
            RationaleDialog()
        } else {
            PermissionDialog { permissionState.launchPermissionRequest() }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    EcommerceTheme {
    }
}
