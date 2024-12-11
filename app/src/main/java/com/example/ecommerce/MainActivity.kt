package com.example.ecommerce

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
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

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class, ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        setContent {
            RequestNotificationPermissionDialog()
            EcommerceTheme {
                val navController = rememberNavController()
                val windowSizeClass = calculateWindowSizeClass(this)
                val navigationType = when (windowSizeClass.widthSizeClass) {
                    WindowWidthSizeClass.Compact -> NavigationType.BOTTOM_NAV
                    WindowWidthSizeClass.Medium -> NavigationType.NAV_RAIL
                    WindowWidthSizeClass.Expanded -> NavigationType.NAV_RAIL
                    else -> NavigationType.BOTTOM_NAV
                }
                RootNavigationGraph(navController = navController, navigationType = navigationType)
            }
        }
    }
}

enum class NavigationType{
    BOTTOM_NAV,NAV_RAIL,PERMANENT_NAV_DRAWER
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

