package com.example.ecommerce.main.wishlist

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.FormatListBulleted
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.ecommerce.R
import com.example.ecommerce.api.model.Product
import com.example.ecommerce.main.cart.CardCart
import com.example.ecommerce.main.detail.ErrorPage
import com.example.ecommerce.main.store.AnimatedGridShimmer
import com.example.ecommerce.main.store.AnimatedListShimmer
import com.example.ecommerce.room.favorite.Favorite
import com.example.ecommerce.ui.theme.Purple

@Composable
fun WishListScreen() {
    val wishViewModel : WishViewModel = hiltViewModel()
    val uiState by wishViewModel.uiState.collectAsStateWithLifecycle()
    val favorite by uiState.favoriteList.collectAsStateWithLifecycle(emptyList())
    var isClickedGrid by rememberSaveable { mutableStateOf(false)}

    Column(Modifier.padding(16.dp)) {
        if(favorite.isEmpty()) {
            ErrorPage(
                title = "Empty",
                message = "Your requested data is unavailable",
                button = R.string.refresh,
                onButtonClick = {},
                alpha = 0F
            )
        }

        Row (modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)) {
            Row(modifier= Modifier.weight(1f),
                horizontalArrangement = Arrangement.Start) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "${favorite.size} Barang",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W400
                )
            }

            Row(modifier= Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Spacer(
                    modifier = Modifier
                        .height(24.dp)
                        .width(1.dp)
                        .background(Color.Gray)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Icon(
                    modifier = Modifier.clickable {
                        isClickedGrid = !isClickedGrid
                    },
                    imageVector = if (isClickedGrid)
                        Icons.Default.GridView else Icons.Default.FormatListBulleted,
                    contentDescription = "List"
                )
            }
        }

        if(isClickedGrid){
            LazyVerticalGrid(GridCells.Fixed(2)) {
                items(favorite){ item->
                    CardGrid(favorite = item){
                        wishViewModel.deleteFavoriteById(it)
                    }
                }
            }
        }else {
            LazyColumn(modifier = Modifier
                .fillMaxSize()
                .background(Color.White)) {
                items(favorite) { item ->
                    CardList(favorite = item){
                        wishViewModel.deleteFavoriteById(it)
                    }
                }
            }
        }
    }
}

@Composable
fun CardList(
    favorite: Favorite,
    onDeleteFavorite : (id:String) -> Unit
){
    Column(
        Modifier
            .padding(vertical = 5.dp)
            .clickable {

            }
    ) {
        Card(modifier = Modifier
            .fillMaxWidth()
            .clickable {

            }, shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(3.dp)
        ) {
            Box(modifier = Modifier.background(Color.White)) {
                Column (modifier = Modifier.padding(10.dp)){
                    Row {
                        Card(
                            modifier = Modifier.size(80.dp),
                            shape = RoundedCornerShape(5.dp)
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                if(favorite.image!!.isEmpty()){
                                    Image(
                                        modifier = Modifier.fillMaxSize(),
                                        painter = painterResource(id = R.drawable.thumbnail),
                                        contentDescription = "image"
                                    )
                                }else{
                                    AsyncImage(
                                        modifier = Modifier.fillMaxSize(),
                                        model = favorite.image, contentDescription = "Favorite Image"
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 10.dp)
                        ) {
                            Text(
                                text = favorite.productName!!,
                                fontWeight = FontWeight.W400,
                                fontSize = 12.sp,
                                lineHeight = 15.sp
                            )
                            Spacer(modifier = Modifier.height(5.dp))
                            Text(
                                text = "Rp${favorite.productPrice}",
                                fontWeight = FontWeight.W600,
                                fontSize = 14.sp
                            )
                            Row(
                                Modifier.padding(top = 5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    modifier = Modifier.size(12.dp),
                                    imageVector = Icons.Filled.AccountCircle,
                                    contentDescription = "Account"
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    text = favorite.store.toString(),
                                    fontWeight = FontWeight.W400,
                                    fontSize = 10.sp
                                )
                            }

                            Row(
                                Modifier.padding(top = 2.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    modifier = Modifier.size(12.dp),
                                    imageVector = Icons.Filled.Star,
                                    contentDescription = "Star"
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    "${favorite.productRating} | Terjual ${favorite.sale}",
                                    fontWeight = FontWeight.W400,
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row {

                        Card(modifier = Modifier
                            .size(32.dp)
                            .clickable {},
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, Color.Gray)
                        ) {
                            Box(modifier = Modifier
                                .fillMaxSize()
                                .background(Color.White),
                                contentAlignment = Alignment.Center) {
                                Icon(
                                    modifier = Modifier.clickable {
                                        onDeleteFavorite(favorite.productId)
                                    },
                                    tint = Purple,
                                    imageVector = Icons.Default.DeleteOutline,
                                    contentDescription = "Delete Favorite"
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        OutlinedButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(32.dp),
                            onClick = { },
                        ) {
                            Text(
                                color = Purple,
                                text = " + Keranjang",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.W500
                            )
                        }
                    }
                }
            }

        }
    }
}


@Composable
fun CardGrid(
    favorite: Favorite,
    onDeleteFavorite : (id:String) -> Unit
){
    Column(Modifier.padding(top = 5.dp, bottom = 5.dp, end = 5.dp)) {
        Card (modifier = Modifier
            .width(186.dp)
            .clickable {},
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(3.dp)
        ){
            Column(Modifier.background(Color.White)) {
                Card(
                    modifier = Modifier.size(186.dp),
                    shape = RoundedCornerShape(
                        topEnd = 8.dp, topStart = 8.dp,
                        bottomEnd = 0.dp, bottomStart = 0.dp)
                ) {
                    Box(modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        if(favorite.image!!.isEmpty()){
                            Image(
                                modifier = Modifier.fillMaxSize(),
                                painter = painterResource(id = R.drawable.thumbnail),
                                contentDescription = "image"
                            )
                        }else{
                            AsyncImage(
                                modifier = Modifier.fillMaxSize(),
                                model = favorite.image, contentDescription = "Favorite Image"
                            )
                        }
                    }
                }

                Column(modifier = Modifier.padding(10.dp)) {
                    Text(
                        text = favorite.productName!!,
                        fontWeight = FontWeight.W400,
                        fontSize = 12.sp,
                        maxLines = 2,
                        lineHeight = 15.sp
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = favorite.productPrice.toString(),
                        fontWeight = FontWeight.W600,
                        fontSize = 14.sp
                    )

                    Row(
                        Modifier.padding(top = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            modifier = Modifier.size(12.dp),
                            imageVector = Icons.Filled.AccountCircle,
                            contentDescription = "Account"
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(text = favorite.store.toString(),
                            fontWeight = FontWeight.W400,
                            fontSize = 10.sp
                        )
                    }

                    Row(
                        Modifier.padding(top = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            modifier = Modifier.size(12.dp),
                            imageVector = Icons.Filled.Star,
                            contentDescription = "Star"
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            "${favorite.productRating} | Terjual ${favorite.sale}",
                            fontWeight = FontWeight.W400,
                            fontSize = 10.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row {
                        Card(modifier = Modifier
                            .size(32.dp)
                            .clickable {},
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, Color.Gray)
                        ) {
                            Box(modifier = Modifier
                                .fillMaxSize()
                                .background(Color.White),
                                contentAlignment = Alignment.Center) {
                                Icon(
                                    modifier = Modifier.clickable {
                                        onDeleteFavorite(favorite.productId)
                                    },
                                    tint = Purple,
                                    imageVector = Icons.Default.DeleteOutline,
                                    contentDescription = "Delete Favorite"
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        OutlinedButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(32.dp),
                            onClick = { },
                        ) {
                            Text(
                                color = Purple,
                                text = " + Keranjang",
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
