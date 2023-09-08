package com.example.ecommerce.main.checkout

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCard
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.twotone.DeleteOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.ecommerce.R
import com.example.ecommerce.api.model.Item
import com.example.ecommerce.api.model.Payment
import com.example.ecommerce.api.response.BaseResponse
import com.example.ecommerce.auth.login.LoginViewModel
import com.example.ecommerce.component.ToastMessage
import com.example.ecommerce.main.payment.PaymentScreen
import com.example.ecommerce.main.store.SearchScreen
import com.example.ecommerce.ui.theme.LightGray
import com.example.ecommerce.ui.theme.Purple
import com.example.ecommerce.ui.theme.textColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    navController: NavHostController,
    choosePayment: () -> Unit,
    productPayment: () -> Unit
){
    Scaffold(
        topBar = {
            TopAppBar(modifier = Modifier.drawBehind {
                val borderSize = 2.dp.toPx()
                drawLine(
                    color = LightGray,
                    start = Offset(0f,size.height),
                    end = Offset(size.width,size.height),
                    strokeWidth = borderSize
                )
            },
                title = {
                    Text(
                        stringResource(id = R.string.checkout),
                        fontSize = 22.sp, color = textColor,
                        fontWeight = FontWeight.Normal)
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(Icons.Default.ArrowBack,"back button")
                    }
                }
            )
        }, bottomBar = {
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
                        text = "Total Bayar",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.W400
                    )

                    Text(
                        text = "Rp23.000.000",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W600
                    )
                }
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                    horizontalAlignment = Alignment.End
                ) {
                    Button(
                        modifier = Modifier, onClick = { productPayment() },
                        colors = ButtonDefaults.buttonColors(Purple)
                    ) {
                        Text(
                            text = "Bayar",
                            fontWeight = FontWeight.W500
                        )
                    }
                }
            }
        }
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(it)
        ) {
            Column (modifier = Modifier.padding(16.dp)){
                Text(
                    text = "Barang yang dibeli",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W500
                )

                Spacer(modifier = Modifier.height(10.dp))

                CardCheckout()
            }

            Divider(thickness = 4.dp)

            Column (modifier = Modifier.padding(16.dp)){
                Text(
                    text = "Pembayaran",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W500
                )

                Spacer(modifier = Modifier.height(10.dp))

                Card (modifier= Modifier
                    .fillMaxWidth()
                    .clickable {
                        choosePayment()
                    },
                    shape = RoundedCornerShape(8.dp),
                    elevation = CardDefaults.cardElevation(3.dp)
                    ) {
                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)) {
                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            ) {
                            Row(modifier = Modifier.weight(1f),
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AddCard,
                                    contentDescription = "Card"
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "Pilih Pembayaran",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.W500
                                )
                            }
                            Row(
                                modifier = Modifier.weight(1f),
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
fun CardCheckout(){
    Row(modifier = Modifier){
            Card(
                modifier = Modifier.size(80.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                        Image(
                            modifier = Modifier.fillMaxSize(),
                            painter = painterResource(id = R.drawable.thumbnail),
                            contentDescription = "image"
                        )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Produk Name",
                    maxLines = 1,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W500
                )
                Text(
                    text = "16 GB",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.W400
                )
                Text(
                    text = "Stok 9",
                    color = Color.Black,
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
                            text = "Rp23.000.000",
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
                                .clickable { },
                            imageVector = Icons.TwoTone.DeleteOutline,
                            contentDescription = " Delete"
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
                                Icon(modifier = Modifier
                                    .size(14.dp)
                                    .clickable {},
                                    imageVector = Icons.Default.Remove,
                                    contentDescription = "Remove"
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "1", fontSize = 12.sp,
                                    fontWeight = FontWeight.W500
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Icon(
                                    modifier = Modifier
                                        .size(14.dp)
                                        .clickable {},
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


@Composable
fun OpenPaymentScreen(
    openDialog: Boolean,
    onCloseDialog: () -> Unit
) {
    if(openDialog){
        Dialog(onDismissRequest = { onCloseDialog() },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
           // PaymentScreen(onCloseDialog)
        }
    }
}