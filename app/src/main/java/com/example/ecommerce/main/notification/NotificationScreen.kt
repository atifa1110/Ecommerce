package com.example.ecommerce.main.notification

import android.content.res.Configuration
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.example.core.room.notification.Notification
import com.example.ecommerce.R
import com.example.ecommerce.main.data.notifications
import com.example.ecommerce.main.detail.ErrorPage
import com.example.ecommerce.ui.theme.EcommerceTheme
import com.example.ecommerce.ui.theme.PurplePink

@Composable
fun NotificationScreen(
    onNavigateBack: () -> Unit
) {
    val notificationViewModel: NotificationViewModel = hiltViewModel()
    val uiState = notificationViewModel.uiState.collectAsStateWithLifecycle()
    val notificationList =
        uiState.value.notificationList.collectAsStateWithLifecycle(emptyList()).value
    val message = uiState.value.message

    NotificationContent(
        onNavigateBack = { onNavigateBack() },
        notificationList = notificationList,
        setNotificationRead = { id, read ->
            notificationViewModel.updateReadNotification(id, read)
        },
        message = message
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationContent(
    onNavigateBack: () -> Unit,
    notificationList: List<Notification>,
    setNotificationRead: (id: Int, read: Boolean) -> Unit,
    message : String
){
    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text(
                            stringResource(id = R.string.notification),
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Normal,
                            color = MaterialTheme.colorScheme.surface
                        )
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.background),
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
        Column(modifier = Modifier.padding(it)) {
            if (notificationList.isEmpty()) {
                ErrorPage(
                    title = stringResource(id = R.string.empty),
                    message = stringResource(id = R.string.resource),
                    button = R.string.refresh,
                    onButtonClick = { /*TODO*/ },
                    alpha = 0f
                )
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(notificationList) { notification ->
                        CardNotification(
                            notification = notification,
                            setNotificationRead = { id, read ->
                                //notificationViewModel.updateReadNotification(id, read)
                                setNotificationRead(id,read)
                            },
                            message
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CardNotification(
    notification: Notification,
    setNotificationRead: (id: Int, read: Boolean) -> Unit,
    message: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                if (notification.isRead) MaterialTheme.colorScheme.background
                else PurplePink
            )
            .clickable {
                notification.id?.let { setNotificationRead(it, true) }
            }
            .padding(top = 16.dp, start = 16.dp)
    ) {
        Card(
            modifier = Modifier.size(36.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                if(notification.image.isEmpty()){
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        painter = painterResource(id = R.drawable.thumbnail),
                        contentDescription = "Card")
                }else{
                    AsyncImage(
                        modifier = Modifier.fillMaxSize(),
                        model = notification.image,
                        contentDescription = "Notification Image"
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(10.dp))

        Column {
            Column(modifier = Modifier.padding(end = 16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = notification.type,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.W400,
                        color = if (notification.isRead)
                            MaterialTheme.colorScheme.surface else Color.Black
                    )

                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            text = "${notification.date}, ${notification.time}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.W400,
                            color = if (notification.isRead)
                                MaterialTheme.colorScheme.surface else Color.Black
                        )
                    }
                }

                Text(
                    text = notification.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W600,
                    color = if (notification.isRead)
                        MaterialTheme.colorScheme.surface else Color.Black
                )

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = notification.body,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.W400,
                    color = if (notification.isRead)
                        MaterialTheme.colorScheme.surface else Color.Black
                )
            }
            Divider(modifier = Modifier.padding(top = 10.dp))
        }
    }
}


@Preview("Light Mode", device = Devices.PIXEL_3)
@Preview("Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CardNotificationPreview() {
    val notification = Notification(
        1,"Telkomsel Award 2023", "Nikmati Kemeriahan ulang tahun Telkomsel pada har jumat 21 Juli 2023 pukul 19.00 - 21.00 WIB langsung dari Beach City International Stadium dengan berbagai kemudahan untuk mendapatkan aksesnya.",
        "", "Promo","21 Jul 2023","12:34", true
    )
    EcommerceTheme {
        CardNotification(
            notification = notification,
            setNotificationRead = { id, read -> },
            message = ""
        )
    }
}

@Preview("Light Mode", device = Devices.PIXEL_3)
@Preview("Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun NotificationContentPreview() {
    EcommerceTheme {
        NotificationContent(
            onNavigateBack = {},
            notificationList = notifications,
            setNotificationRead = { _, _ -> },
            message = ""
        )
    }
}
