package com.example.ecommerce.main.home

import android.app.LocaleConfig
import android.app.LocaleManager
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.os.LocaleListCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.ecommerce.R
import com.example.ecommerce.auth.login.LoginViewModel
import com.example.ecommerce.main.main.MainViewModel
import com.example.ecommerce.ui.theme.EcommerceTheme
import com.example.ecommerce.ui.theme.Purple

@Composable
fun HomeScreen(
    onLogoutClick: () -> Unit
) {
    val homeViewModel : HomeViewModel = hiltViewModel()
    val context = LocalContext.current

    Column (modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        val animationLottie by rememberLottieComposition(
            spec = LottieCompositionSpec.RawRes(resId = R.raw.animation)
        )
        val animationAction by animateLottieCompositionAsState(
            composition = animationLottie,
            iterations = LottieConstants.IterateForever
        )
        LottieAnimation(modifier = Modifier
            .fillMaxWidth()
            .height(182.dp),
            composition = animationLottie,
            progress = { animationAction }
        )

        Button(
            onClick = {
                homeViewModel.saveAccessToken(token = "")
                homeViewModel.saveLoginState(complete = false)
                onLogoutClick()
            },
            modifier = Modifier.padding(vertical = 16.dp),
            colors = ButtonDefaults.buttonColors(Purple),
            enabled =  true
        ) {
            Text(
                text = stringResource(id = R.string.logout),
                style = MaterialTheme.typography.labelLarge
            )
        }

        val default = AppCompatDelegate.getApplicationLocales()[0]?.language ?: "en"
        val defaultLanguage = default == "in"
        val checkedState = rememberSaveable{ mutableStateOf(defaultLanguage) }
        Log.d("CheckState",checkedState.toString())
        Row (modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center){
            Text(text = stringResource(id = R.string.en),style = MaterialTheme.typography.bodyMedium)
            Switch(
                modifier = Modifier.padding(horizontal = 10.dp),
                checked = checkedState.value,
                onCheckedChange = {
                    checkedState.value = it
                    if(checkedState.value){
                        val appEnglish : LocaleListCompat = LocaleListCompat.forLanguageTags("in")
                        AppCompatDelegate.setApplicationLocales(appEnglish)
                    }else{
                        val appLocale : LocaleListCompat = LocaleListCompat.forLanguageTags("en")
                        AppCompatDelegate.setApplicationLocales(appLocale)
                    }
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = Purple,
                    uncheckedThumbColor = Purple,
                    uncheckedTrackColor = Color.White,
                ),
            )
            Text(text = stringResource(id = R.string.id), style = MaterialTheme.typography.bodyMedium)
        }

        val isSystemInDarkTheme = isSystemInDarkTheme()
        val darkMode = remember { mutableStateOf(isSystemInDarkTheme) }

        Row (modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center){
            Text(text = stringResource(id = R.string.light),style = MaterialTheme.typography.bodyMedium)
            Switch(
                modifier = Modifier.padding(horizontal = 10.dp),
                checked = darkMode.value,
                onCheckedChange = {
                    darkMode.value = it
                    if (darkMode.value) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    } else {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    }
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = Purple,
                    uncheckedThumbColor = Purple,
                    uncheckedTrackColor = Color.White,
                ),
            )
            Text(text = stringResource(id = R.string.dark), style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview(){
    EcommerceTheme {
        Surface {
            HomeScreen {}
        }
    }
}