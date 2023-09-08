package com.example.ecommerce.main.status

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
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.ecommerce.R
import com.example.ecommerce.ui.theme.LightGray
import com.example.ecommerce.ui.theme.LightPurple
import com.example.ecommerce.ui.theme.Purple
import com.example.ecommerce.ui.theme.textColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatusScreen(navController : NavHostController){
    val ratingState = remember { mutableStateOf(0) }
    val inputReview = remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(modifier = Modifier.drawBehind {
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
                        stringResource(id = R.string.status),
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

                    Button(
                        modifier = Modifier.fillMaxWidth(), onClick = { },
                        colors = ButtonDefaults.buttonColors(Purple)
                    ) {
                        Text(
                            text = "Selesai",
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
            Column(modifier = Modifier.padding(16.dp)) {

                Box(modifier = Modifier.fillMaxWidth()) {
                    Card(modifier = Modifier.padding(top=30.dp),
                        shape = RoundedCornerShape(8.dp),
                        elevation = CardDefaults.cardElevation(3.dp)) {
                        Column(modifier = Modifier
                            .background(Color.White)
                            .padding(top=40.dp, start = 16.dp,end=16.dp, bottom = 16.dp)) {
                            Column(modifier=Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "Pembayaran Berhasil",
                                    color = Purple,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.W600
                                )
                            }

                            Column(modifier=Modifier.fillMaxWidth().padding(top = 10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally) {
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
                                text = "Beri Ulasan",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.W600
                            )
                            Spacer(modifier = Modifier.height(10.dp))

                            TextField(input = inputReview)
                        }
                    }
                    Box(modifier = Modifier
                        .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Card(shape = CircleShape) {
                            Column(
                                modifier = Modifier
                                    .background(LightPurple)
                                    .padding(16.dp)
                            ) {
                                Icon(tint = Purple,
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
                    text = "Detail Transaksi",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W600
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row {
                    Text(text = "ID Transaksi" , fontSize = 12.sp, fontWeight = FontWeight.W400)
                    Row(modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.End
                        ) {
                        Text(text = "abcd-efgh-ijkl-mnop", fontSize = 12.sp, fontWeight = FontWeight.W600)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row {
                    Text(text = "Status" , fontSize = 12.sp, fontWeight = FontWeight.W400)
                    Row(modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(text = "Berhasil", fontSize = 12.sp, fontWeight = FontWeight.W600)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row {
                    Text(text = "Tanggal" , fontSize = 12.sp, fontWeight = FontWeight.W400)
                    Row(modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(text = "3 Jun 2023", fontSize = 12.sp, fontWeight = FontWeight.W600)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row {
                    Text(text = "Waktu" , fontSize = 12.sp, fontWeight = FontWeight.W400)
                    Row(modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(text = "22:00", fontSize = 12.sp, fontWeight = FontWeight.W600)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row {
                    Text(text = "Metode Pembayaran" , fontSize = 12.sp, fontWeight = FontWeight.W400)
                    Row(modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(text = "BCA Virtual Account", fontSize = 12.sp, fontWeight = FontWeight.W600)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row {
                    Text(text = "Total Pembayaran" , fontSize = 12.sp, fontWeight = FontWeight.W400)
                    Row(modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(text = "Rp23.499.000", fontSize = 12.sp, fontWeight = FontWeight.W600)
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
    input : MutableState<String>,
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
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = imeAction,
            keyboardType = KeyboardType.Number
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                localFocusManager.clearFocus()
            }
        ),
    )
}

@Composable
@Preview(showBackground = true)
fun StatusScreenPreview(){
    StatusScreen(rememberNavController())
}