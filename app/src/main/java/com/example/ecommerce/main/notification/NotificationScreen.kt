package com.example.ecommerce.main.notification

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
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
        }
    ) {
        Column(modifier = Modifier.padding(it)) {

        }
    }
}

@Composable
@Preview(showBackground = true)
fun CardNotification(){
    Column {
        Text(text = stringResource(id = R.string.info))
        Text(text = stringResource(id = R.string.transaction_done))
        Text(text = stringResource(id = R.string.dummy_notification))
    }
}

