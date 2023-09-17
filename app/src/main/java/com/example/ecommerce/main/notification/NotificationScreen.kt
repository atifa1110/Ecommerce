package com.example.ecommerce.main.notification

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconButton
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCard
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.ecommerce.R
import com.example.ecommerce.api.request.FulfillmentRequest
import com.example.ecommerce.ui.theme.LightGray
import com.example.ecommerce.ui.theme.Purple
import com.example.ecommerce.ui.theme.textColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(
    navController : NavHostController
) {
    Scaffold(
        topBar = {
            Column{
            TopAppBar(
                title = {
                    Text(
                        stringResource(id = R.string.notification),
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
                Divider()
            }
        }
    ) {
        Column(modifier = Modifier.padding(it)) {

            LazyColumn(modifier = Modifier.fillMaxSize()){
                repeat(3){
                    item{ CardNotification() }
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun CardNotification(){
        Row (modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, start = 16.dp)){
            Card(
                modifier = Modifier.size(36.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        painter = painterResource(id = R.drawable.thumbnail),
                        contentDescription = "Card"
                    )
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column {
                Column (modifier= Modifier.padding(end = 16.dp)){
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = stringResource(id = R.string.info),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.W400
                        )

                        Row(
                            modifier = Modifier.weight(1f),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Text(
                                text = "12 Jun 2023, 11:45",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.W400
                            )
                        }
                    }

                    Text(
                        text = stringResource(id = R.string.transaction_done),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W600
                    )

                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(id = R.string.dummy_notification),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.W400
                    )
                }
                Divider(modifier = Modifier.padding(vertical = 10.dp))
            }
        }
}

