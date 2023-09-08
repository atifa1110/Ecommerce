package com.example.ecommerce.main.transaction

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecommerce.main.detail.ErrorPage
import com.example.ecommerce.R
import com.example.ecommerce.ui.theme.LightPurple
import com.example.ecommerce.ui.theme.Purple

@Composable
@Preview(showBackground = true)
fun TransactionScreen() {
    Column(modifier = Modifier.fillMaxSize()) {
//        ErrorPage(title ="Empty",
//            message = "Your requested data is unavailable",
//            button = R.string.refresh,
//            onButtonClick = {  },
//            alpha =1F)

        LazyColumn(modifier = Modifier.fillMaxSize().padding(top = 11.dp)) {
            repeat(2){
                item {
                    CardTransaction()
                }
            }
        }
    }
}

@Composable
fun CardTransaction() {
    Column (Modifier.padding(top=5.dp, start = 16.dp,end=16.dp)){
        Card(modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)) {
            Column(modifier = Modifier.fillMaxWidth()) {
                //Column Belanja
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
                                    text = "Belanja",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.W600
                                )
                                Text(
                                    text = "4 Jun 2023",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.W400
                                )
                            }

                        }

                        Column(
                            modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.End
                        ) {
                            Card(
                                modifier = Modifier
                                    .height(18.dp)
                                    .width(46.dp),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(LightPurple),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Selesai",
                                        color = Purple,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.W600
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
                            Box(modifier = Modifier.fillMaxSize()) {
                                Image(
                                    modifier = Modifier.fillMaxSize(),
                                    painter = painterResource(id = R.drawable.thumbnail),
                                    contentDescription = "Image"
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = "Lenovo Legion 7 16 I7 11800 16GB 1TB SSD..",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.W500
                            )
                            Text(
                                text = "1 barang",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.W400
                            )
                        }
                    }


                    Row(modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = "Total Belanja",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.W400
                            )
                            Text(
                                text = "Rp23.000.000",
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
                                    .height(24.dp)
                                    .width(84.dp),
                                shape = RoundedCornerShape(100.dp)
                            ) {
                                Box(modifier = Modifier
                                        .fillMaxSize()
                                        .background(Purple),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        color = Color.White,
                                        text = "Ulas",
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
