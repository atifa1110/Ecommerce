package com.example.ecommerce.main.checkout

import android.net.Uri
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCard
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.core.api.model.Item
import com.example.core.api.request.FulfillmentRequest
import com.example.core.api.response.BaseResponse
import com.example.core.room.cart.Cart
import com.example.core.room.cart.CartItem
import com.example.core.room.cart.ListCheckout
import com.example.ecommerce.R
import com.example.ecommerce.main.detail.currency
import com.example.ecommerce.ui.theme.Purple
import com.google.gson.Gson
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    onNavigateBack: () -> Unit,
    listCheckout: ListCheckout?,
    choosePayment: () -> Unit,
    onClickToPayment: (fulfillment: String) -> Unit,
    paymentItem: Item?
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }
    val checkoutViewModel: CheckoutViewModel = hiltViewModel()
    var isLoading by remember { mutableStateOf(false) }
    val checkoutItem: List<Cart> = listCheckout?.listCheckout ?: emptyList()
    val cartItem: List<CartItem> =
        checkoutItem.map { CartItem(it.productId, it.productVariantName, it.quantity) }
    val uiState by checkoutViewModel.uiState.collectAsStateWithLifecycle()
    checkoutViewModel.beginToCheckOutAnalytics(checkoutItem)

    val totalPrice = checkoutItem.sumOf {
        it.productPrice!!.plus(it.productVariantPrice!!)
            .times(it.quantity!!)
    }

    val total = remember { mutableStateOf(totalPrice) }

    checkoutViewModel.checkOutResult.observe(lifecycleOwner) {
        when (it) {
            is BaseResponse.Loading -> {
                isLoading = true
            }

            is BaseResponse.Success -> {
                isLoading = false
                val checkoutResult = it.data?.data
                val jsonCheckout = Uri.encode(Gson().toJson(checkoutResult))
                onClickToPayment(jsonCheckout)
                checkoutViewModel.purchase(checkoutResult)
            }

            is BaseResponse.Error -> {
                isLoading = false
                scope.launch {
                    snackBarHostState.showSnackbar(it.msg.toString())
                }
            }

            else -> {}
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        },
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text(
                            stringResource(id = R.string.checkout),
                            style = MaterialTheme.typography.titleLarge
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            onNavigateBack()
                        }) {
                            Icon(Icons.Default.ArrowBack, "back button")
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
                    Text(
                        text = stringResource(id = R.string.total),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.W400
                    )

                    Text(
                        text = currency(total.value),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W600
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalAlignment = Alignment.End
                ) {
                    Button(
                        modifier = Modifier,
                        onClick = {
                            checkoutViewModel.fulfillment(
                                FulfillmentRequest(
                                    paymentItem!!.label,
                                    cartItem
                                )
                            )
                            checkoutViewModel.buttonAnalytics("Pay Button")
                        },
                        colors = ButtonDefaults.buttonColors(Purple),
                        enabled = paymentItem != null
                    ) {
                        Text(
                            text = stringResource(id = R.string.pay),
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
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    CircularProgressIndicator(color = Purple)
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = stringResource(id = R.string.purchased_items),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W500
                )

                Spacer(modifier = Modifier.height(10.dp))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                ) {
                    items(checkoutItem) { item ->
                        CardCheckout(
                            cart = item,
                            addQuantity = { it ->
                                checkoutViewModel.addQuantity(item, it)
                            },
                            total,
                            item.quantity ?: 1,
                            checkoutItem
                        )
                    }
                }
            }

            Divider(thickness = 4.dp)

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = stringResource(id = R.string.payment),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W500
                )

                Spacer(modifier = Modifier.height(10.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            choosePayment()
                            checkoutViewModel.addPaymentInfo(paymentItem?.label ?: "")
                        },
                    shape = RoundedCornerShape(8.dp),
                    elevation = CardDefaults.cardElevation(3.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.weight(1f),
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (paymentItem == null) {
                                    Icon(
                                        imageVector = Icons.Default.AddCard,
                                        contentDescription = "Card"
                                    )
                                } else {
                                    AsyncImage(
                                        modifier = Modifier.size(48.dp, 32.dp),
                                        model = paymentItem.image,
                                        contentDescription = "Payment Image"
                                    )
                                }

                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = if (paymentItem == null) stringResource(id = R.string.choose_payment) else paymentItem.label!!,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.W500
                                )
                            }
                            Row(
                                horizontalArrangement = Arrangement.End
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowForwardIos,
                                    contentDescription = "Arrow"
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CardCheckout(
    cart: Cart,
    addQuantity: (quantity: Int) -> Unit,
    totalPrice: MutableState<Int>,
    quantity: Int,
    checkoutItem: List<Cart>
) {
    var count by remember { mutableStateOf(quantity) }

    Row(modifier = Modifier.padding(vertical = 8.dp)) {
        Card(
            modifier = Modifier.size(80.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                if (cart.image!!.isEmpty()) {
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        painter = painterResource(id = R.drawable.thumbnail),
                        contentDescription = "image"
                    )
                } else {
                    AsyncImage(
                        modifier = Modifier.fillMaxSize(),
                        model = cart.image,
                        contentDescription = "Cart Image"
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = cart.productName!!,
                maxLines = 1,
                fontSize = 14.sp,
                fontWeight = FontWeight.W500
            )
            Text(
                text = cart.productVariantName!!,
                fontSize = 10.sp,
                fontWeight = FontWeight.W400
            )
            Text(
                text = if (cart.stock!! > 9) "Stok ${cart.stock}" else "Sisa ${cart.stock}",
                color = if (cart.stock!! > 9) Color.Black else Color.Red,
                fontSize = 10.sp,
                fontWeight = FontWeight.W400
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalAlignment = Alignment.Start
                ) {
                    val variantTotal = cart.productPrice?.plus(cart.productVariantPrice ?: 0)
                    Text(
                        text = currency(variantTotal ?: 0),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W500
                    )
                }

                Row(
                    modifier = Modifier
                        .background(Color.White)
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalArrangement = Arrangement.End
                ) {
                    Card(
                        modifier = Modifier
                            .height(20.dp)
                            .width(72.dp)
                            .background(Color.White),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, Color.Gray)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.White),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                modifier = Modifier
                                    .size(14.dp)
                                    .clickable {
                                        count = if (count > 1) count - 1 else count
                                        var number = 0
                                        checkoutItem.map {
                                            if (it.productId == cart.productId) {
                                                it.quantity = count
                                            }
                                            number += it.productPrice
                                                ?.plus(
                                                    it.productVariantPrice ?: 0
                                                )!!
                                                .times(it.quantity!!)
                                        }
                                        totalPrice.value = number
                                    },
                                imageVector = Icons.Default.Remove,
                                contentDescription = "Remove"
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = count.toString(),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.W500
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Icon(
                                modifier = Modifier
                                    .size(14.dp)
                                    .clickable {
                                        count =
                                            if (count >= 1 && count != cart.stock) count + 1 else if (count == cart.stock) count else 1
                                        addQuantity(count)
                                        var number = 0
                                        checkoutItem.map {
                                            if (it.productId == cart.productId) {
                                                it.quantity = count
                                            }
                                            number += it.productPrice
                                                ?.plus(
                                                    it.productVariantPrice ?: 0
                                                )!!
                                                .times(it.quantity!!)
                                        }
                                        totalPrice.value = number
                                    },
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add"
                            )
                        }
                    }
                }
            }
        }
    }
}
