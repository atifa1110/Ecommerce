package com.example.ecommerce.boarding

import androidx.annotation.DrawableRes
import com.example.ecommerce.R

sealed class OnBoardingPage(
    @DrawableRes
    val image: Int,
) {
    object First : OnBoardingPage(
        image = R.drawable.page1,
    )

    object Second : OnBoardingPage(
        image = R.drawable.page2,
    )

    object Third : OnBoardingPage(
        image = R.drawable.page3,
    )
}
