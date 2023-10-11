package com.example.ecommerce.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = primaryDark,
    onPrimary = onPrimaryDark,
    secondary = secondaryDark,
    onSecondary = onSecondaryDark,
    tertiary = tertiaryDark,
    onTertiary = onTertiaryDark,
    error = errorDark,
    onError = onErrorDark,
    primaryContainer = primaryContainerDark,
    onPrimaryContainer = onPrimaryContainerDark,
    secondaryContainer = secondaryContainerDark,
    onSecondaryContainer = onSecondaryContainerDark,
    tertiaryContainer = tertiaryContainerDark,
    onTertiaryContainer = onTertiaryContainerDark,
    errorContainer = errorContainerDark,
    onErrorContainer = onErrorContainerDark,
    surface = surfaceDark,
    onSurface = onSurfaceDark,
    onSurfaceVariant = onSurfaceVarDark,
    outline = outlineDark,
    outlineVariant = outlineVariantDark,
    inverseOnSurface = inverseSurfaceDark,
    inversePrimary = inversePrimaryDark,
    background = Color.Black
)

private val LightColorScheme = lightColorScheme(
//    primary = primary,
//    onPrimary = onPrimary,
//    secondary = secondary,
//    onSecondary = onSecondary,
//    tertiary = tertiary,
//    onTertiary = onTertiary,
//    error = error,
//    onError = onError,
//    primaryContainer = primaryContainer,
//    onPrimaryContainer = onPrimaryContainer,
//    secondaryContainer = secondaryContainer,
//    onSecondaryContainer = onSecondaryContainer,
//    tertiaryContainer = tertiaryContainer,
//    onTertiaryContainer = onTertiaryContainer,
//    errorContainer = errorContainer,
//    onErrorContainer = onErrorContainer,
    surface = Color.White,
    background = Color.White
//    onSurface = onSurface,
//    onSurfaceVariant = onSurfaceVar,
//    outline = outline,
//    outlineVariant = outlineVariant,
//    inverseOnSurface = inverseSurface,
//    inversePrimary = inversePrimary

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
     */
)

@Composable
fun EcommerceTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
