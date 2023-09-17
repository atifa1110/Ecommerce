package com.example.ecommerce.main.cart

import android.net.Uri
import android.util.Log
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
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.twotone.DeleteOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.ecommerce.R
import com.example.ecommerce.api.model.Item
import com.example.ecommerce.main.detail.ErrorPage
import com.example.ecommerce.main.detail.currency
import com.example.ecommerce.room.cart.Cart
import com.example.ecommerce.room.cart.ListCheckout
import com.example.ecommerce.ui.theme.LightGray
import com.example.ecommerce.ui.theme.Purple
import com.example.ecommerce.ui.theme.textColor
import com.google.gson.Gson

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    navController: NavHostController,
    onCheckOut:(listCheck : String) -> Unit
){
    val cardViewModel : CartViewModel = hiltViewModel()

    val uiState by cardViewModel.uiState.collectAsStateWithLifecycle()
    val cart = uiState.cartList.collectAsStateWithLifecycle(emptyList()).value
    val total = uiState.total

    val cartSelected = uiState.cartSelected.collectAsStateWithLifecycle(emptyList()).value
    val jsonCheckout = Uri.encode(Gson().toJson(ListCheckout(cartSelected)))
    cardViewModel.checkSelected(cart.size)
    cardViewModel.getCheckedSelected()

    val buttonVisible by cardViewModel.buttonVisible.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text(stringResource(id = R.string.cart),
                            style = MaterialTheme.typography.titleLarge
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            navController.popBackStack()
                            cardViewModel.selectedAllCart(false)
                        }) {
                            Icon(Icons.Default.ArrowBack, "back button")
                        }
                    }
                )
                Divider()
            }
        }, bottomBar = {
            if(cart.isNotEmpty()) {
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
                        cardViewModel.getTotal()
                        Text(
                            text = currency(total!!),
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
                                onCheckOut(jsonCheckout)
                            },
                            colors = ButtonDefaults.buttonColors(Purple),
                            enabled = buttonVisible
                        ) {
                            Text(
                                text = stringResource(id = R.string.buy),
                                fontWeight = FontWeight.W500
                            )
                        }
                    }
                }
            }
        }
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            if(cart.isEmpty()){
                Column(modifier = Modifier
                    .background(Color.White)
                    .fillMaxSize()) {
                    ErrorPage(
                        title = stringResource(id = R.string.empty),
                        message = stringResource(id = R.string.resource),
                        button = R.string.refresh,
                        onButtonClick = {},
                        0f
                    )
                }
            }

            Column(modifier = Modifier
                .fillMaxSize()
                .padding(it)) {
                val checkedState by cardViewModel.selectedAll.collectAsStateWithLifecycle()

                Row(modifier = Modifier.padding(start = 5.dp, end = 8.dp)) {
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                        horizontalArrangement = Arrangement.Start
                        ,verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(checked = checkedState,
                            onCheckedChange = {
                                cardViewModel.selectedAllCart(it)
                            },
                            colors = CheckboxDefaults.colors(Purple)
                        )
                        Text(text = stringResource(id = R.string.choose_all))
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        TextButton(
                            modifier = Modifier.alpha(if(buttonVisible) 1f else 0f),
                            onClick = {cardViewModel.deletedCartBySelected()}) {
                            Text(
                                text = stringResource(id = R.string.erase),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.W500
                            )
                        }
                    }
                }

                Divider()

                LazyColumn(modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)) {
                    items(cart) { item ->
                        CardCart(
                            cart = item,
                            onChecked = {
                                cardViewModel.selectedCart(item, it)
                            },
                            onDeleteCart = {
                                cardViewModel.deleteCartById(item.productId)
                            },
                            addQuantity = {
                                cardViewModel.addQuantity(item,it)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CardCart(
    cart : Cart,
    onDeleteCart: () -> Unit,
    onChecked: (checked : Boolean) -> Unit,
    addQuantity: (quantity: Int) -> Unit
){
    var count by remember { mutableStateOf(cart.quantity)}

    Column(modifier = Modifier){
        Row(modifier = Modifier
            .padding(top = 16.dp, start = 5.dp, end = 16.dp)
            .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = cart.selected!!,
                onCheckedChange = {
                    onChecked(it)
                },
                colors = CheckboxDefaults.colors(Purple)
            )

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
                            model = cart.image, contentDescription = "Cart Image"
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
                    text = if(cart.stock!! > 9) "Stok ${cart.stock}" else "Sisa ${cart.stock}",
                    color = if(cart.stock > 9) Color.Black else Color.Red,
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
                        Text(
                            text = currency(cart.productPrice!!),
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
                        Icon(
                            modifier = Modifier
                                .size(20.dp)
                                .clickable { onDeleteCart() },
                            imageVector = Icons.TwoTone.DeleteOutline,
                            contentDescription = "Delete"
                        )
                        Spacer(modifier = Modifier.width(10.dp))
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
                                            count = if (count!! > 0) count!! - 1 else 0
                                            addQuantity(count!!)
                                        },
                                    imageVector = Icons.Default.Remove,
                                    contentDescription = "Remove"
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = count.toString(), fontSize = 12.sp,
                                    fontWeight = FontWeight.W500
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Icon(
                                    modifier = Modifier
                                        .size(14.dp)
                                        .clickable {
                                            count =
                                                if (count!! >= 1 && count != cart.stock) count!! + 1 else if (count == cart.stock) count else 1
                                            addQuantity(count!!)
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
}

@Composable
@Preview(showBackground = true)
fun CartScreenPreview(){
    val controller = rememberNavController()
    //CartScreen(controller,{})
}

@Composable
@Preview(showBackground = true)
fun CardCartPreview() {
    Column (modifier = Modifier
        .fillMaxSize()
        .background(Color.White)){
    }
}