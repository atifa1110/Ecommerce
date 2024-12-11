package com.example.ecommerce.main.cart

import android.content.res.Configuration
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
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.twotone.DeleteOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.core.room.cart.Cart
import com.example.core.room.cart.ListCheckout
import com.example.ecommerce.R
import com.example.ecommerce.main.detail.ErrorPage
import com.example.ecommerce.main.detail.currency
import com.example.ecommerce.ui.theme.EcommerceTheme
import com.example.ecommerce.ui.theme.Purple
import com.example.ecommerce.ui.theme.PurplePink
import com.google.gson.Gson

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    onNavigateBack: () -> Unit,
    onCheckOut: (listCheck: String) -> Unit
) {
    val cardViewModel: CartViewModel = hiltViewModel()

    val uiState by cardViewModel.uiState.collectAsStateWithLifecycle()
    val cart = uiState.cartList.collectAsStateWithLifecycle(emptyList()).value
    val total = uiState.total

    val cartSelected = uiState.cartSelected.collectAsStateWithLifecycle(emptyList()).value
    val jsonCheckout = Uri.encode(Gson().toJson(ListCheckout(cartSelected)))

    cardViewModel.checkSelected(cart.size)
    cardViewModel.getCheckedSelected()
    cardViewModel.viewCartAnalytics(cart)

    val buttonVisible by cardViewModel.buttonVisible.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text(
                            stringResource(id = R.string.cart),
                            style = MaterialTheme.typography.titleLarge
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            onNavigateBack()
                            cardViewModel.selectedAllCart(false)
                        }) {
                            Icon(Icons.Default.ArrowBack, "back button")
                        }
                    }
                )
                Divider()
            }
        },
        bottomBar = {
            if (cart.isNotEmpty()) {
                HorizontalDivider()
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
                                cardViewModel.buttonAnalytics("Buy Button")
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
            if (cart.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    ErrorPage(
                        title = stringResource(id = R.string.empty),
                        message = stringResource(id = R.string.resource),
                        button = R.string.refresh,
                        onButtonClick = {},
                        0f
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                val checkedState by cardViewModel.selectedAll.collectAsStateWithLifecycle()

                Row(modifier = Modifier.padding(start = 5.dp, end = 8.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = checkedState,
                            onCheckedChange = {
                                cardViewModel.selectedAllCart(it)
                            },
                            colors = CheckboxDefaults.colors(Purple)
                        )
                        Text(text = stringResource(id = R.string.choose_all))
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        TextButton(
                            modifier = Modifier.alpha(if (buttonVisible) 1f else 0f),
                            onClick = {
                                cardViewModel.deletedCartBySelected()
                            }
                        ) {
                            Text(
                                text = stringResource(id = R.string.erase),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.W500
                            )
                        }
                    }
                }

                Divider()

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                ) {
                    items(cart) { item ->
                        CardCart(
                            cart = item,
                            onChecked = {
                                cardViewModel.selectedCart(item, it)
                            },
                            onDeleteCart = {
                                cardViewModel.deleteCartById(item.productId)
                                cardViewModel.removeCartAnalytics(item.productId)
                            },
                            addQuantity = {
                                cardViewModel.addQuantity(item, it)
                            }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartContent(
    onNavigateBack: () -> Unit,
    cart : List<Cart>,
    getTotal: () -> Unit,
    total : Int,
    onCheckOut: () -> Unit,
    onBuyButtonAnalytics: () -> Unit,
    buttonVisible : Boolean,
    checkedState : Boolean,
    selectedAllCart: (selected: Boolean) -> Unit,
    deletedCartBySelected: () -> Unit,
    selectedCart : (cart: Cart, selected:Boolean) -> Unit,
    deletedCartById : (id: String) -> Unit,
    removeCartAnalytics : (id: String) -> Unit,
    addQuantity : (item: Cart, quantity: Int) -> Unit
){
    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text(
                            stringResource(id = R.string.cart),
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.surface
                        )
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.background),
                    navigationIcon = {
                        IconButton(onClick = {
                            onNavigateBack()
                            selectedAllCart(false)
                            //cardViewModel.selectedAllCart(false)
                        }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, "Arrow Back")
                        }
                    }
                )
                Divider()
            }
        },
        bottomBar = {
            if (cart.isNotEmpty()) {
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
                        //cardViewModel.getTotal()
                        getTotal()
                        Text(
                            text = currency(total?:0),
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
                                //onCheckOut(jsonCheckout)
                                //cardViewModel.buttonAnalytics("Buy Button")
                                onCheckOut()
                                onBuyButtonAnalytics()
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Purple,
                                contentColor = Color.White
                            ),
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
            if (cart.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    ErrorPage(
                        title = stringResource(id = R.string.empty),
                        message = stringResource(id = R.string.resource),
                        button = R.string.refresh,
                        onButtonClick = {},
                        0f
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                //val checkedState by cardViewModel.selectedAll.collectAsStateWithLifecycle()

                Row(modifier = Modifier.padding(start = 5.dp, end = 8.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = checkedState,
                            onCheckedChange = {
                                //cardViewModel.selectedAllCart(it)
                                selectedAllCart(it)
                            },
                            colors = CheckboxDefaults.colors(
                                checkedColor = Purple,
                                uncheckedColor = MaterialTheme.colorScheme.surface
                            )
                        )
                        Text(text = stringResource(id = R.string.choose_all))
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        TextButton(
                            modifier = Modifier.alpha(if (buttonVisible) 1f else 0f),
                            onClick = {
                               // cardViewModel.deletedCartBySelected()
                                deletedCartBySelected()
                            }
                        ) {
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
                ) {
                    items(cart) { item ->
                        CardCart(
                            cart = item,
                            onChecked = {
                                //cardViewModel.selectedCart(item, it)
                                selectedCart(item,it)
                            },
                            onDeleteCart = {
                                //cardViewModel.deleteCartById(item.productId)
                                //cardViewModel.removeCartAnalytics(item.productId)
                                deletedCartById(item.productId)
                                removeCartAnalytics(item.productId)
                            },
                            addQuantity = {
                                //cardViewModel.addQuantity(item, it)
                                addQuantity(item,it)
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
    cart: Cart,
    onDeleteCart: () -> Unit,
    onChecked: (checked: Boolean) -> Unit,
    addQuantity: (quantity: Int) -> Unit
) {
    var count by remember { mutableStateOf(cart.quantity) }

    Column(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
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
                colors = CheckboxDefaults.colors(
                    checkedColor = Purple,
                    uncheckedColor = MaterialTheme.colorScheme.surface
                )
            )

            Card(
                modifier = Modifier.size(80.dp),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.surface
                )
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
                    fontWeight = FontWeight.W500,
                    color = MaterialTheme.colorScheme.surface
                )
                Text(
                    text = cart.productVariantName!!,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.W400,
                    color = MaterialTheme.colorScheme.surface
                )
                Text(
                    text = if (cart.stock!! > 9) "Stok ${cart.stock}" else "Sisa ${cart.stock}",
                    color = if (cart.stock!! > 9) MaterialTheme.colorScheme.surface else Color.Red,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.W400,
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                        horizontalAlignment = Alignment.Start
                    ) {
                        val variantPrice =
                            cart.productPrice?.plus(cart.productVariantPrice ?: 0) ?: 0
                        Text(
                            text = currency(variantPrice),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W500,
                            color = MaterialTheme.colorScheme.surface
                        )
                    }

                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Icon(
                            modifier = Modifier
                                .size(20.dp)
                                .clickable { onDeleteCart() },
                            imageVector = Icons.TwoTone.DeleteOutline,
                            contentDescription = "Delete",
                            tint = MaterialTheme.colorScheme.surface
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Card(
                            modifier = Modifier
                                .height(20.dp)
                                .width(72.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.background
                            ),
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(1.dp, Color.Gray)
                        ) {
                            Row(modifier = Modifier
                                    .fillMaxSize(),
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

@Preview("Light Mode", device = Devices.PIXEL_3)
@Preview("Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CardCartPreview(){
    val cart = Cart("1","Lenovo 3", 1200000,"","Lenovo","Laptop ini","LenovoStore",21,12,5,12,12,5.0f,"Lenovo White",500000,2,false)
    EcommerceTheme {
        CardCart(cart = cart, onDeleteCart = { /*TODO*/ }, onChecked = {}, addQuantity = {})
    }
}

@Preview("Light Mode", device = Devices.PIXEL_3)
@Preview("Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CartScreenPreview() {
    val carts = listOf(
        Cart("1","Lenovo Ideapad Slim 31 14 i3 1115G4 8GB 256SSD W11 + OHS 14.0 2YR F31B - ABYSS BLUE, SSD 256 GB"
            ,6049000,"","Lenovo","This Laptop",
            "LenovoStore",12,12,5,15,2,5.0f,
            "Lenovo White",1000000,5,true),
        Cart("2","Lenovo Ideapad Slim 31 14 i3 1115G4 8GB 256SSD W11 + OHS 14.0 2YR F31B - ABYSS BLUE, SSD 256 GB"
            ,6049000,"","Lenovo","This Laptop",
            "LenovoStore",12,12,5,15,2,5.0f,
            "Lenovo White",1000000,5,false)
    )
    EcommerceTheme {
        CartContent(
            onNavigateBack = { /*TODO*/ },
            cart = carts,
            getTotal = { /*TODO*/ },
            total = 12000000,
            onCheckOut = { /*TODO*/ },
            onBuyButtonAnalytics = { /*TODO*/ },
            buttonVisible = true,
            checkedState = false,
            selectedAllCart = {},
            deletedCartBySelected = { /*TODO*/ },
            selectedCart = {cart, selected ->  },
            deletedCartById = {} ,
            removeCartAnalytics = {},
            addQuantity = {item, quantity ->  }
        )
    }
}
