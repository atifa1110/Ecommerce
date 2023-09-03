package com.example.ecommerce.boarding

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ecommerce.R
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
    onSkipClick: () -> Unit
) {

    val pages = listOf(OnBoardingPage.First, OnBoardingPage.Second, OnBoardingPage.Third)

    val pagerState = rememberPagerState()

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
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Column(Modifier.weight(1f), horizontalAlignment = Alignment.Start
            ){
                TextSkipButton(R.string.skip, onSkipClick)
            }
            Column(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HorizontalPagerIndicator(modifier = Modifier.padding(5.dp), pagerState = pagerState)
            }
            Column(Modifier.weight(1f), horizontalAlignment = Alignment.End
            ) {
                TextNextButton(R.string.next,pages.size,pagerState)
            }
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

@Composable
fun TextSkipButton(label : Int, onClick: () -> Unit){
    TextButton(onClick = onClick,modifier = Modifier
    ) {
        Text(text = stringResource(id = label), fontSize = 14.sp, color = Purple)
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TextNextButton(label : Int, size: Int, pageState : PagerState){
    val scope = rememberCoroutineScope()
    AnimatedVisibility(visible = pageState.currentPage != 2 ) {
        TextButton(onClick = {
            if (pageState.currentPage + 1 < size) scope.launch {
                pageState.scrollToPage(pageState.currentPage + 1)
            }
        },modifier = Modifier
        ) {
            Text(text = stringResource(id = label), fontSize = 14.sp, color = Purple)
        }
    }
}

@ExperimentalAnimationApi
@ExperimentalPagerApi
@Composable
@Preview(showBackground = true)
fun OnBoardingPreview(){
    onBoardingScreen(onJoinClick = { }) {}
}