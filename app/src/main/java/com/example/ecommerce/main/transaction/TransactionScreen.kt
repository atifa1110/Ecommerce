package com.example.ecommerce.main.transaction

import android.content.res.Configuration
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.core.api.model.Fulfillment
import com.example.core.api.model.Transaction
import com.example.core.api.response.BaseResponse
import com.example.core.room.cart.CartItem
import com.example.ecommerce.R
import com.example.ecommerce.main.data.mockTransactions
import com.example.ecommerce.main.detail.ErrorPage
import com.example.ecommerce.main.detail.currency
import com.example.ecommerce.ui.theme.EcommerceTheme
import com.example.ecommerce.ui.theme.LightPurple
import com.example.ecommerce.ui.theme.Purple
import com.google.gson.Gson

@Composable
fun TransactionScreen(
    onNavigateToStatus: (transaction: String) -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val transactionViewModel: TransactionViewModel = hiltViewModel()
    var isLoading by remember { mutableStateOf(false) }
    var isError by remember { mutableStateOf(false) }
    var listTransaction: List<Transaction> = emptyList()

    transactionViewModel.transactionResult.observe(lifecycleOwner) {
        when (it) {
            is BaseResponse.Loading -> {
                isLoading = true
                isError = false
            }

            is BaseResponse.Success -> {
                isLoading = false
                isError = false
                listTransaction = it.data!!.data
            }

            is BaseResponse.Error -> {
                isLoading = false
                isError = true
            }

            else -> {}
        }
    }

    TransactionContent(isError = isError,
        isLoading = isLoading,
        listTransaction = listTransaction,
        onNavigateToStatus = {
            onNavigateToStatus(it)
        },
        onButtonAnalytics = {
            transactionViewModel.buttonAnalytics("Review Button")
        }
    )
}

@Composable
fun TransactionContent(
    isError : Boolean,
    isLoading : Boolean,
    listTransaction: List<Transaction>,
    onNavigateToStatus: (transaction: String) -> Unit,
    onButtonAnalytics: () -> Unit,
){
    Column(modifier = Modifier.fillMaxSize()
        .background(MaterialTheme.colorScheme.background)) {
        if (isLoading) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                CircularProgressIndicator(color = Purple)
            }
        }

        if (isError) {
            ErrorPage(
                title = stringResource(id = R.string.empty),
                message = stringResource(id = R.string.resource),
                button = R.string.refresh,
                onButtonClick = { },
                alpha = 1F
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 10.dp)
        ) {
            items(listTransaction) { item ->
                CardTransaction(
                    transaction = item,
                    onNavigateToStatus = onNavigateToStatus,
                    onButtonAnalytics = onButtonAnalytics
                )
            }
        }
    }
}

@Composable
fun CardTransaction(
    transaction: Transaction,
    onNavigateToStatus: (transaction: String) -> Unit,
    onButtonAnalytics: () -> Unit,
    //transactionViewModel: TransactionViewModel
) {
    Log.d("TransactionData", transaction.toString())
    Column(Modifier.padding(top = 8.dp, start = 16.dp, end = 16.dp)) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(3.dp),
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Column Belanja
                Column(
                    modifier = Modifier.padding(
                        top = 10.dp,
                        bottom = 10.dp,
                        start = 16.dp,
                        end = 16.dp
                    ),
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            modifier = Modifier.weight(1f),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Icon(
                                modifier = Modifier.size(28.dp),
                                imageVector = Icons.Outlined.ShoppingBag,
                                contentDescription = "Shopping Bag"
                            )

                            Spacer(modifier = Modifier.width(10.dp))

                            Column {
                                Text(
                                    text = stringResource(id = R.string.shopping),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.W600
                                )
                                Text(
                                    text = transaction.date,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.W400
                                )
                            }
                        }

                        Column(
                            modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.End
                        ) {
                            Card(modifier = Modifier
                                    .height(20.dp)
                                    .width(46.dp),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Box(modifier = Modifier
                                        .fillMaxSize()
                                        .background(LightPurple),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = stringResource(id = R.string.done),
                                        color = Purple,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.W600,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                }
                Divider()
                Column(
                    modifier = Modifier.padding(
                        top = 10.dp,
                        bottom = 10.dp,
                        start = 16.dp,
                        end = 16.dp
                    )
                ) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Card(
                            modifier = Modifier.size(40.dp),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Box(modifier = Modifier.size(40.dp)) {
                                if (transaction.image.isEmpty()) {
                                    Image(
                                        modifier = Modifier.fillMaxSize(),
                                        painter = painterResource(id = R.drawable.thumbnail),
                                        contentDescription = "Image"
                                    )
                                } else {
                                    AsyncImage(
                                        modifier = Modifier.fillMaxSize(),
                                        model = transaction.image,
                                        contentDescription = "Transaction image"
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = transaction.name,
                                maxLines = 1,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.W500
                            )
                            val item: List<Int> = transaction.items.map { it.quantity!! }
                            Text(
                                text = "${item.sum()} barang",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.W400
                            )
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = stringResource(id = R.string.total_spend),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.W400
                            )
                            Text(
                                text = currency(transaction.total),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.W600
                            )
                        }

                        Column(
                            modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.End
                        ) {
                            Card(
                                modifier = Modifier
                                    .alpha(
                                        if (transaction.rating == "0" && transaction.review == "" ||
                                            transaction.rating == null && transaction.review == null
                                        ) {
                                            1f
                                        } else {
                                            0f
                                        }
                                    )
                                    .clickable {
                                        val fulfillment = Fulfillment(
                                            transaction.invoiceId,
                                            transaction.status,
                                            transaction.date,
                                            transaction.time,
                                            transaction.payment,
                                            transaction.total
                                        )
                                        val jsonFulfillment = Uri.encode(Gson().toJson(fulfillment))
                                        onNavigateToStatus(jsonFulfillment)
                                        onButtonAnalytics()
                                        //transactionViewModel.buttonAnalytics("Review Button")
                                    }
                                    .height(24.dp)
                                    .width(84.dp),
                                shape = RoundedCornerShape(100.dp),
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(Purple),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        color = Color.White,
                                        text = stringResource(id = R.string.review2),
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.W500
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


@Preview("Light Mode", device = Devices.PIXEL_3)
@Preview("Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun TransactionScreenPreview(){

    EcommerceTheme {
        TransactionContent(
            isError = false,
            isLoading = false,
            listTransaction = mockTransactions,
            onNavigateToStatus = {},
            onButtonAnalytics = {}
        )
    }
}