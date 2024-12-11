package com.example.ecommerce.main.status

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.IconButton
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.core.api.model.Fulfillment
import com.example.core.api.model.Rating
import com.example.core.api.response.BaseResponse
import com.example.ecommerce.R
import com.example.ecommerce.main.data.fulfillment
import com.example.ecommerce.main.detail.currency
import com.example.ecommerce.ui.theme.EcommerceTheme
import com.example.ecommerce.ui.theme.LightPurple
import com.example.ecommerce.ui.theme.Purple

@Composable
fun StatusScreen(
    fulfillment: Fulfillment?,
    onNavigateToHome: () -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val statusViewModel: StatusViewModel = hiltViewModel()
    var isLoading by remember { mutableStateOf(false) }
    val ratingState = remember { mutableStateOf(0) }
    val inputReview = remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }

    Log.d("FulfillmentItem", fulfillment.toString())

    statusViewModel.ratingResult.observe(lifecycleOwner) {
        when (it) {
            is BaseResponse.Loading -> {
                isLoading = true
            }

            is BaseResponse.Success -> {
                isLoading = false
                result = it.data?.message?:""
                onNavigateToHome()
            }

            is BaseResponse.Error -> {
                isLoading = false
            }

            else -> {}
        }
    }
    StatusContent(
        onNavigateToHome = { onNavigateToHome() },
        ratingStatus = {
            statusViewModel.ratingStatus(
                Rating(
                    invoiceId = fulfillment?.invoiceId?:"",
                    rating = ratingState.value,
                    review = inputReview.value
                )
            )
        },
        buttonDoneAnalytics = {
            statusViewModel.buttonAnalytics("Done")
        },
        isLoading = isLoading,
        ratingState = ratingState,
        inputReview = inputReview,
        fulfillment = fulfillment
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatusContent(
    onNavigateToHome: () -> Unit,
    ratingStatus: () -> Unit,
    buttonDoneAnalytics: () -> Unit,
    isLoading : Boolean,
    ratingState: MutableState<Int>,
    inputReview: MutableState<String>,
    fulfillment: Fulfillment?
){
    Scaffold(
        topBar = {
            Column {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            stringResource(id = R.string.status),
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.surface
                        )
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.background),
                    navigationIcon = {
                        IconButton(onClick = {
                            onNavigateToHome()
                        }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, "back button")
                        }
                    }
                )
                Divider()
            }
        },
        bottomBar = {
            Divider()
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalAlignment = Alignment.Start
                ) {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            ratingStatus()
                            buttonDoneAnalytics()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Purple,
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = stringResource(id = R.string.done),
                            fontWeight = FontWeight.W500
                        )
                    }
                }
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            if (isLoading) {
                Column(modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    CircularProgressIndicator(color = Purple)
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Card(
                        modifier = Modifier.padding(top = 30.dp),
                        shape = RoundedCornerShape(8.dp),
                        elevation = CardDefaults.cardElevation(3.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Column(modifier = Modifier
                            .padding(top = 40.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = stringResource(id = R.string.payment_success),
                                    color = MaterialTheme.colorScheme.primary,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.W600
                                )
                            }

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                RatingBar(
                                    maxRating = 5,
                                    rating = ratingState,
                                    onRatingChanged = { newRating ->
                                        ratingState.value = newRating
                                    }
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = stringResource(id = R.string.leave_review),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.W600
                            )
                            Spacer(modifier = Modifier.height(10.dp))

                            TextField(input = inputReview)
                        }
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Card(shape = CircleShape) {
                            Column(
                                modifier = Modifier
                                    .background(LightPurple)
                                    .padding(16.dp)
                            ) {
                                Icon(
                                    tint = Purple,
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Check"
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.detail_transaction),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W600
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row {
                    Text(
                        text = stringResource(id = R.string.transaction_id),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.W400
                    )
                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            text = fulfillment?.invoiceId?:"",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.W600
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row {
                    Text(
                        text = stringResource(id = R.string.status),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.W400
                    )
                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(text = stringResource(R.string.Success), fontSize = 12.sp, fontWeight = FontWeight.W600)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row {
                    Text(
                        text = stringResource(id = R.string.date),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.W400
                    )
                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            text = fulfillment?.date?:"",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.W600
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row {
                    Text(
                        text = stringResource(id = R.string.time),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.W400
                    )
                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            text = fulfillment?.time?:"",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.W600
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row {
                    Text(
                        text = stringResource(id = R.string.payment_method),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.W400
                    )
                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            text = fulfillment?.payment?:"",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.W600
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row {
                    Text(
                        text = stringResource(id = R.string.total_payment),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.W400
                    )
                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            text = currency(fulfillment!!.total),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.W600
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RatingBar(
    maxRating: Int = 5,
    rating: MutableState<Int>,
    onRatingChanged: (Int) -> Unit,
    activeColor: Color = Color.DarkGray,
    inactiveColor: Color = Color.LightGray,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        for (i in 1..maxRating) {
            val isFilled = i <= rating.value
            val iconColor = if (isFilled) activeColor else inactiveColor

            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier
                    .size(44.dp)
                    .clickable {
                        rating.value = i
                        onRatingChanged(i)
                    }
                    .padding(4.dp)
            )
        }
    }
}

@Composable
fun TextField(
    input: MutableState<String>,
    imeAction: ImeAction = ImeAction.Done,
) {
    val localFocusManager = LocalFocusManager.current
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = input.value,
        onValueChange = {
            input.value = it
        },
        textStyle = MaterialTheme.typography.bodyMedium,
        keyboardOptions = KeyboardOptions(
            imeAction = imeAction,
            keyboardType = KeyboardType.Text
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                localFocusManager.clearFocus()
            }
        ),
    )
}


@Preview("Light Mode", device = Devices.PIXEL_3)
@Preview("Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun StatusScreenPreview() {
    val ratingState = remember { mutableStateOf(0) }
    val inputReview = remember { mutableStateOf("") }
    EcommerceTheme {
        StatusContent(
            onNavigateToHome = {},
            ratingStatus = {},
            buttonDoneAnalytics = {},
            isLoading = false,
            ratingState = ratingState,
            inputReview = inputReview,
            fulfillment = fulfillment
        )
    }
}
