package com.example.ecommerce.boarding

import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ecommerce.R
import com.example.ecommerce.main.main.MainViewModel
import com.example.ecommerce.ui.theme.Purple
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.*
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

@ExperimentalAnimationApi
@ExperimentalPagerApi
@Composable
fun onBoardingScreen(
    onJoinClick: () -> Unit,
    onSkipClick: () -> Unit,
    onNavigateToLogin:() -> Unit
) {

    val pages = listOf(OnBoardingPage.First, OnBoardingPage.Second, OnBoardingPage.Third)
    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()

    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        HorizontalPager(modifier = Modifier.weight(1f),
            count = 3, state = pagerState,
            verticalAlignment = Alignment.CenterVertically
        ) { position ->
            PagerScreen(onBoardingPage = pages[position])
        }

        ButtonJoin(onJoinClick)

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            //column button skip
            Column(Modifier.weight(1f), horizontalAlignment = Alignment.Start
            ){
                TextButton(onClick = onSkipClick,modifier = Modifier
                ) {
                    Text(text = stringResource(id = R.string.skip), fontSize = 14.sp, color = Purple)
                }
            }
            //colum pager indicator
            Column(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HorizontalPagerIndicator(modifier = Modifier.padding(5.dp), pagerState = pagerState)
            }
            //column button next
            Column(Modifier.weight(1f), horizontalAlignment = Alignment.End
            ) {
                TextButton(onClick = {
                    if (pagerState.currentPage + 1 < pages.size) scope.launch {
                        pagerState.scrollToPage(pagerState.currentPage + 1)
                    }
                },modifier = Modifier.alpha(if(pagerState.currentPage == 2) 0f else 1f)
                ) {
                    Text(text = stringResource(id = R.string.next), fontSize = 14.sp, color = Purple)
                }
            }
        }
    }
}

@ExperimentalAnimationApi
@ExperimentalPagerApi
@Composable
fun PagerScreen(onBoardingPage: OnBoardingPage){
    Column() {
        Image(modifier = Modifier
            .fillMaxSize(),
            painter = painterResource(id = onBoardingPage.image),
            contentDescription = "Pager Image"
        )
    }
}

@Composable
fun ButtonJoin(onClick: () -> Unit){
    Button(onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = ButtonDefaults.buttonColors(Purple),
        enabled =  true
    ) {
        Text(
            text = stringResource(id = R.string.gabung),
            fontWeight = FontWeight.W500
        )
    }
}

@ExperimentalAnimationApi
@ExperimentalPagerApi
@Composable
@Preview(showBackground = true)
fun OnBoardingPreview(){
    onBoardingScreen(onJoinClick = { /*TODO*/ }, onSkipClick = { /*TODO*/ }) {

    }
}