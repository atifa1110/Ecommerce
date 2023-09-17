package com.example.ecommerce.main.payment

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCard
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.ecommerce.R
import com.example.ecommerce.api.model.Item
import com.example.ecommerce.api.model.Payment
import com.example.ecommerce.api.response.BaseResponse
import com.example.ecommerce.api.response.DetailResponse
import com.example.ecommerce.component.ToastMessage
import com.example.ecommerce.main.checkout.CheckoutViewModel
import com.example.ecommerce.ui.theme.LightGray
import com.example.ecommerce.ui.theme.Purple
import com.example.ecommerce.ui.theme.textColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(
    onNavigateBack : () -> Unit,
    onItemClick : (payment: Item) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val paymentViewModel : PaymentViewModel = hiltViewModel()
    var isLoading by remember { mutableStateOf(false) }
    var result by remember { mutableStateOf(emptyList<Payment>())}

    paymentViewModel.paymentResult.observe(lifecycleOwner) {
        when (it) {
            is BaseResponse.Loading -> {
                isLoading = true
            }

            is BaseResponse.Success -> {
                isLoading = false
                result = it.data!!.data
                //ToastMessage().showMsg(context,it.data.message)
            }

            is BaseResponse.Error -> {
                isLoading = false
                ToastMessage().showMsg(context,it.msg.toString())
            }

            else -> {}
        }
    }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text(
                            stringResource(id = R.string.choose_payment),
                            fontSize = 22.sp, color = textColor,
                            fontWeight = FontWeight.Normal
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
        }
    ) {
        Column(modifier = Modifier
            .padding(it)
            .background(Color.White)
        ) {
            if (isLoading)
                Column(modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    CircularProgressIndicator(color = Purple)
                }

            LazyColumn (modifier = Modifier.fillMaxSize()){
                if (result.isNotEmpty()) {
                    items(result.size) { index ->
                        PaymentComposable(result[index], onItemClick)
                    }
                }
            }
        }
    }
}

@Composable
fun PaymentComposable(payment: Payment, onItemClick: (payment: Item) -> Unit) {
    Column(modifier = Modifier) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 16.dp)) {
            Text(
                text = payment.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.W500
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Column {
            payment.item.forEach { virtual ->
                Column {
                    CardPayment(virtual, onItemClick)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Divider(thickness = 4.dp)
    }
}


@Composable
fun CardPayment(item: Item, onItemClick: (payment: Item) -> Unit){
    Column{
        Card (modifier= Modifier
            .clickable {
                onItemClick(item)
            }
            .fillMaxWidth()
            .padding(start = 16.dp)) {
            Column(modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)) {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, end = 16.dp, bottom = 16.dp),
                    verticalAlignment = Alignment.CenterVertically) {
                    Row(modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically) {
                        if(item.image!!.isEmpty()){
                            Icon(imageVector = Icons.Default.AddCard,
                                contentDescription = "Card")
                        }else{
                            AsyncImage(modifier = Modifier.size(48.dp,32.dp),
                                model = item.image,
                                contentDescription = "Image Item")
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Text(modifier = Modifier.weight(1f),
                            text = if(item.label!!.isEmpty()) "Pilih Pembayaran" else item.label,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W500
                        )
                    }

                    Row(horizontalArrangement = Arrangement.End
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowForwardIos,
                            contentDescription = "Arrow"
                        )
                    }
                }
            }
        }
        Divider(modifier = Modifier.padding(start = 16.dp))
    }
}
