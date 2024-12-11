package com.example.ecommerce.main.wishlist

import android.content.res.Configuration
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
import com.example.core.room.favorite.Favorite
import com.example.ecommerce.R
import com.example.ecommerce.main.data.mockFavorites
import com.example.ecommerce.main.detail.ErrorPage
import com.example.ecommerce.main.detail.currency
import com.example.ecommerce.ui.theme.EcommerceTheme
import com.example.ecommerce.ui.theme.Purple
import kotlinx.coroutines.launch

@Composable
fun WishListScreen() {
    val wishViewModel: WishViewModel = hiltViewModel()
    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }
    val uiState by wishViewModel.uiState.collectAsStateWithLifecycle()
    val favorite by uiState.favoriteList.collectAsStateWithLifecycle(emptyList())
    val isClickedGrid by rememberSaveable { mutableStateOf(false) }
    val isLoading by remember { mutableStateOf(false) }

    val isFavorite = favorite.isEmpty()

    uiState.message?.let { message ->
        scope.launch {
            snackBarHostState.showSnackbar(message)
        }
    }

    uiState.isLoading.let {}

//    Scaffold(
//        snackbarHost = {
//            SnackbarHost(hostState = snackBarHostState)
//        },
//    ) {
//        Column(
//            Modifier
//                .padding(it)
//                .padding(16.dp)
//        ) {
//            if (favorite.isEmpty()) {
//                ErrorPage(
//                    title = stringResource(id = R.string.empty),
//                    message = stringResource(id = R.string.resource),
//                    button = R.string.refresh,
//                    onButtonClick = {},
//                    alpha = 0F
//                )
//            }
//
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(bottom = 10.dp)
//            ) {
//                Row(
//                    modifier = Modifier.weight(1f),
//                    horizontalArrangement = Arrangement.Start
//                ) {
//                    Text(
//                        modifier = Modifier.fillMaxWidth(),
//                        text = "${favorite.size} " + stringResource(id = R.string.item),
//                        fontSize = 14.sp,
//                        fontWeight = FontWeight.W400
//                    )
//                }
//
//                Row(
//                    modifier = Modifier.weight(1f),
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.End
//                ) {
//                    Spacer(
//                        modifier = Modifier
//                            .height(24.dp)
//                            .width(1.dp)
//                            .background(Color.Gray)
//                    )
//                    Spacer(modifier = Modifier.width(10.dp))
//                    Icon(
//                        modifier = Modifier.clickable {
//                            isClickedGrid = !isClickedGrid
//                        },
//                        imageVector = if (isClickedGrid) {
//                            Icons.Default.GridView
//                        } else Icons.Default.FormatListBulleted,
//                        contentDescription = "List"
//                    )
//                }
//            }
//
//            if (isClickedGrid) {
//                LazyVerticalGrid(GridCells.Fixed(2)) {
//                    items(favorite) { item ->
//                        CardGrid(
//                            favorite = item,
//                            onDeleteFavorite = {
//                                wishViewModel.deleteFavoriteById(it)
//                            },
//                            onAddToCart = {
//                                wishViewModel.addFavoriteToCart(item)
//                            }
//                        )
//                    }
//                }
//            } else {
//                LazyColumn(modifier = Modifier.fillMaxSize()
//                ) {
//                    items(favorite) { item ->
//                        CardList(
//                            favorite = item,
//                            onDeleteFavorite = { it ->
//                                wishViewModel.deleteFavoriteById(it)
//                            },
//                            onAddToCart = {
//                                wishViewModel.addFavoriteToCart(item)
//                            }
//                        )
//                    }
//                }
//            }
//        }
//    }

    WishListContent(
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        },
        isFavorite = isFavorite,
        isClickedGrid = isClickedGrid,
        favorite = favorite,
        onAddToCart = { fav ->
            wishViewModel.addFavoriteToCart(fav)
        },
        onDeleteFavorite = { id->
            wishViewModel.deleteFavoriteById(id)
        }
    )
}


@Composable
fun WishListContent(
    snackbarHost: @Composable () -> Unit,
    isFavorite : Boolean,
    isClickedGrid : Boolean,
    favorite : List<Favorite>,
    onAddToCart: (favorite : Favorite) -> Unit,
    onDeleteFavorite: (id: String) -> Unit
){
    var isClickedGrid by rememberSaveable { mutableStateOf(isClickedGrid) }

    Scaffold(
        snackbarHost = snackbarHost,
    ) {
        Column(
            Modifier
                .padding(it)
                .padding(16.dp)
        ) {
            if(isFavorite){
                ErrorPage(
                    title = stringResource(id = R.string.empty),
                    message = stringResource(id = R.string.resource),
                    button = R.string.refresh,
                    onButtonClick = {},
                    alpha = 0F
                )
            }

            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp)
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "${favorite.size} " + stringResource(id = R.string.item),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W400
                    )
                }

                Row(
                    modifier = Modifier.weight(1f),
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
                        imageVector = if (isClickedGrid) {
                            Icons.Default.GridView
                        } else Icons.Default.FormatListBulleted,
                        contentDescription = "List"
                    )
                }
            }

            if (isClickedGrid) {
                LazyVerticalGrid(GridCells.Fixed(2)) {
                    items(favorite) { item ->
                        CardGrid(
                            favorite = item,
                            onDeleteFavorite = {
                               //wishViewModel.deleteFavoriteById(it)
                               onDeleteFavorite(it)
                            },
                            onAddToCart = {
                               // wishViewModel.addFavoriteToCart(item)
                                onAddToCart(item)
                            }
                        )
                    }
                }
            } else {
                LazyColumn(modifier = Modifier
                        .fillMaxSize()
                ) {
                    items(favorite) { item ->
                        CardList(
                            favorite = item,
                            onDeleteFavorite = { it ->
                               // wishViewModel.deleteFavoriteById(it)
                                onDeleteFavorite(it)
                            },
                            onAddToCart = {
                               // wishViewModel.addFavoriteToCart(item)
                                onAddToCart(item)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CardList(
    favorite: Favorite,
    onAddToCart: () -> Unit,
    onDeleteFavorite: (id: String) -> Unit
) {
    Column(
        Modifier.padding(vertical = 5.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(3.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Box {
                Column(modifier = Modifier.padding(10.dp)) {
                    Row {
                        Card(
                            modifier = Modifier.size(80.dp),
                            shape = RoundedCornerShape(5.dp)
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                if (favorite.image!!.isEmpty()) {
                                    Image(
                                        modifier = Modifier.fillMaxSize(),
                                        painter = painterResource(id = R.drawable.thumbnail),
                                        contentDescription = "image"
                                    )
                                } else {
                                    AsyncImage(
                                        modifier = Modifier.fillMaxSize(),
                                        model = favorite.image,
                                        contentDescription = "Favorite Image"
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))

                        Column(modifier = Modifier
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
                            val variantTotal =
                                favorite.productPrice?.plus(favorite.productVariantPrice ?: 0)

                            Text(
                                text = currency(variantTotal ?: 0),
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
                        Card(
                            modifier = Modifier
                                .size(32.dp)
                                .clickable {},
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, Color.Gray),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            ),
                        ) {
                            Box(modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    modifier = Modifier.clickable {
                                        onDeleteFavorite(favorite.productId)
                                    },
                                    tint = MaterialTheme.colorScheme.primary,
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
                            onClick = { onAddToCart() },
                        ) {
                            Text(
                                text = stringResource(id = R.string.cartplus),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.W500,
                                color = MaterialTheme.colorScheme.primary,
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
    onAddToCart: () -> Unit,
    onDeleteFavorite: (id: String) -> Unit
) {
    Column(Modifier.padding(top = 5.dp, bottom = 5.dp, end = 5.dp)) {
        Card(modifier = Modifier.width(186.dp),
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(3.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column {
                Card(modifier = Modifier.size(186.dp),
                    shape = RoundedCornerShape(
                        topEnd = 8.dp,
                        topStart = 8.dp,
                        bottomEnd = 0.dp,
                        bottomStart = 0.dp
                    )
                ) {
                    Box(modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        if (favorite.image!!.isEmpty()) {
                            Image(
                                modifier = Modifier.fillMaxSize(),
                                painter = painterResource(id = R.drawable.thumbnail),
                                contentDescription = "image",
                                contentScale = ContentScale.FillBounds
                            )
                        } else {
                            AsyncImage(
                                modifier = Modifier.fillMaxSize(),
                                model = favorite.image,
                                contentDescription = "Favorite Image",
                                contentScale = ContentScale.FillBounds
                            )
                        }
                    }
                }

                Column(modifier = Modifier.padding(10.dp)) {
                    Text(
                        text = favorite.productName ?: "",
                        fontWeight = FontWeight.W400,
                        fontSize = 12.sp,
                        maxLines = 2,
                        lineHeight = 15.sp
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    val variantTotal =
                        favorite.productPrice?.plus(favorite.productVariantPrice ?: 0)
                    Text(
                        text = currency(variantTotal ?: 0),
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

                    Spacer(modifier = Modifier.height(10.dp))

                    Row {
                        Card(
                            modifier = Modifier
                                .size(32.dp)
                                .clickable {},
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, Color.Gray),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                            )
                        ) {
                            Box(modifier = Modifier
                                    .fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    modifier = Modifier.clickable {
                                        onDeleteFavorite(favorite.productId)
                                    },
                                    tint = MaterialTheme.colorScheme.primary,
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
                            onClick = { onAddToCart() },
                        ) {
                            Text(
                                text = stringResource(R.string.cartplus),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.W500,
                                color = MaterialTheme.colorScheme.primary,
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
@Preview("Light Mode", device = Devices.PIXEL_3)
fun CardPreview(){
    EcommerceTheme {
        Column {
            CardList(favorite = mockFavorites[2],
                onAddToCart = {}, onDeleteFavorite = {})

            CardGrid(favorite = mockFavorites[2],
                onAddToCart = {}, onDeleteFavorite = {})
        }
    }
}

@Composable
@Preview("Light Mode", device = Devices.PIXEL_3)
@Preview("Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
fun WishListGridPreview(){
    EcommerceTheme {
        WishListContent(
            snackbarHost = { /*TODO*/ },
            isFavorite = false,
            isClickedGrid = true,
            favorite = mockFavorites,
            onAddToCart = {},
            onDeleteFavorite = {}
        )
    }
}

@Composable
@Preview("Light Mode", device = Devices.PIXEL_3)
@Preview("Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
fun WishListListPreview(){
    EcommerceTheme {
        WishListContent(
            snackbarHost = { /*TODO*/ },
            isFavorite = false,
            isClickedGrid = false,
            favorite = mockFavorites,
            onAddToCart = {},
            onDeleteFavorite = {}
        )
    }
}