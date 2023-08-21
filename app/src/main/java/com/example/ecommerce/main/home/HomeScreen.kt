package com.example.ecommerce.main.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ecommerce.R
import com.example.ecommerce.ui.theme.Purple

@Composable
fun HomeScreen(onLogoutClick: () -> Unit) {
    Column (modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Button(
            onClick = { onLogoutClick() },
            modifier = Modifier
                .padding(vertical = 16.dp),
            colors = ButtonDefaults.buttonColors(Purple),
            enabled =  true
        ) {
            Text(
                text = stringResource(id = R.string.logout),
                fontWeight = FontWeight.W500
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview(){
    HomeScreen {
    }
}