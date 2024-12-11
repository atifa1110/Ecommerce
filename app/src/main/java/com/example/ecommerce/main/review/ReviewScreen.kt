package com.example.ecommerce.main.review

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.core.api.response.BaseResponse
import com.example.core.api.response.ReviewResponse
import com.example.ecommerce.R
import com.example.ecommerce.main.data.mockReviews
import com.example.ecommerce.main.detail.ErrorPage
import com.example.ecommerce.ui.theme.EcommerceTheme
import com.example.ecommerce.ui.theme.PurplePink

@Composable
fun ReviewScreen(
    id: String,
    onNavigateBack: () -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val reviewViewModel: ReviewViewModel = hiltViewModel()
    var isSuccess by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var isError by remember { mutableStateOf(false) }
    var result by remember { mutableStateOf(emptyList<ReviewResponse.Review>()) }

    LaunchedEffect(key1 = true) {
        reviewViewModel.getProductReview(id)
    }

    reviewViewModel.reviewResult.observe(lifecycleOwner) {
        when (it) {
            is BaseResponse.Loading -> {
                isLoading = true
                isError = false
                isSuccess = false
            }

            is BaseResponse.Success -> {
                isLoading = false
                isError = false
                isSuccess = true
                result = it.data?.data?: emptyList()
            }

            is BaseResponse.Error -> {
                isLoading = false
                isSuccess = false
                isError = true
            }

            else -> {}
        }
    }

    ReviewContent(
        isLoading = isLoading,
        isError = isError,
        isSuccess = isSuccess,
        result = result,
        onNavigateBack = { onNavigateBack() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewContent(
    isLoading : Boolean,
    isError : Boolean,
    isSuccess : Boolean,
    result : List<ReviewResponse.Review>,
    onNavigateBack: () -> Unit
){
    var isLoading by remember { mutableStateOf(isLoading) }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text(
                            stringResource(id = R.string.review_buyer),
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.surface
                        )
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.background),
                    navigationIcon = {
                        IconButton(onClick = { onNavigateBack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, "back button")
                        }
                    }
                )
                Divider()
            }
        },
    ) {
        if (isSuccess) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(result) { index ->
                        CardReview(index)
                    }
                }
            }
        }

        if (isError) {
            ErrorPage(
                title = stringResource(id = R.string.empty),
                message = stringResource(id = R.string.resource),
                button = R.string.refresh,
                onButtonClick = {
                    isLoading = true
                },
                1f
            )
        }

        if (isLoading) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
fun CardReview(review: ReviewResponse.Review) {
    Column(Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(PurplePink),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (review.userImage?.isEmpty() == true) {
                    Text(text = "A",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W500
                    )
                } else {
                    AsyncImage(
                        modifier = Modifier.size(36.dp),
                        model = review.userImage,
                        contentDescription = "user image"
                    )
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column {
                Text(text = review.userName?:"",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.W600,
                    color = MaterialTheme.colorScheme.surface
                )

                val ratingState = remember { mutableStateOf(review.userRating) }

                RatingBar(
                    maxRating = 5,
                    rating = ratingState
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))
        Text(text = review.userReview?:"",
            fontSize = 12.sp,
            fontWeight = FontWeight.W400,
            color = MaterialTheme.colorScheme.surface
        )
        Spacer(modifier = Modifier.height(10.dp))
    }

    Divider()
}

@Composable
fun RatingBar(
    maxRating: Int = 5,
    rating: MutableState<Int?>,
    activeColor: Color = Color.DarkGray,
    inactiveColor: Color = Color.LightGray,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        for (i in 1..maxRating) {
            val isFilled = i <= rating.value!!
            val iconColor = if (isFilled) activeColor else inactiveColor

            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = "Rating Product",
                tint = iconColor,
                modifier = Modifier.size(12.dp)
            )
        }
    }
}

@Preview("Light Mode", device = Devices.PIXEL_3)
@Preview("Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CardReviewPreview() {
    EcommerceTheme {
        CardReview(review = mockReviews[0])
    }
}

@Preview("Light Mode", device = Devices.PIXEL_3)
@Preview("Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ReviewContentPreview() {
    EcommerceTheme {
        ReviewContent(
            isLoading = false,
            isError = false,
            isSuccess = true,
            result = mockReviews,
            onNavigateBack = {}
        )
    }
}


