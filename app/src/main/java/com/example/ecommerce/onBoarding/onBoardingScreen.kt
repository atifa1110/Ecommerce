package com.example.ecommerce.onBoarding

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.ecommerce.R
import com.example.ecommerce.ui.theme.EnableButton
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.*
import com.google.accompanist.pager.rememberPagerState

@ExperimentalAnimationApi
@ExperimentalPagerApi
@Composable
fun onBoardingScreen(
    onJoinClick: () -> Unit,
    onSkipClick: () -> Unit) {
    val pages = listOf(
        OnBoardingPage.First,
        OnBoardingPage.Second, OnBoardingPage.Third)

    val pagerState = rememberPagerState()

    Column(modifier = Modifier.fillMaxSize(),
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

        Row(modifier = Modifier.fillMaxWidth()
            .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            TextSkipButton("Lewati", onSkipClick)
            HorizontalPagerIndicator(modifier = Modifier
                .padding(5.dp),
                pagerState = pagerState)
            TextNextButton("Selanjutnya", pagerState.currentPage==3)
        }
    }
}

@ExperimentalAnimationApi
@ExperimentalPagerApi
@Composable
fun PagerScreen(onBoardingPage: OnBoardingPage){
    Column(modifier = Modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Image(modifier = Modifier
            .fillMaxWidth()
            .height(550.dp),
            painter = painterResource(id = onBoardingPage.image),
            contentDescription = "Pager Image"
        )
    }
}

@Composable
fun ButtonJoin(onClick: () -> Unit){
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Button(onClick = onClick,
            modifier = Modifier
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(EnableButton),
            enabled =  true
        ) {
            Text(
                text = stringResource(id = R.string.gabung),
                fontWeight = FontWeight.W500
            )
        }
    }
}

@Composable
fun TextSkipButton(label : String, onClick: () -> Unit){
    TextButton(onClick = onClick,modifier = Modifier
    ) {
        Text(text = label, fontSize = 14.sp, color = EnableButton)
    }
}

@Composable
fun TextNextButton(label : String, isNotVisible : Boolean){
    TextButton(modifier = Modifier
        .fillMaxWidth(),
        enabled = true,
        onClick = {}
    ) {
        Text(text = label, fontSize = 14.sp, color = EnableButton)
    }
}

@ExperimentalAnimationApi
@ExperimentalPagerApi
@Composable
@Preview(showBackground = true)
fun OnBoardingPreview(){
    onBoardingScreen(onJoinClick = { }) {
    }
}