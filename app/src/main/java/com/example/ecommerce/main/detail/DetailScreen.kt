package com.example.ecommerce.main.detail

import android.util.Log
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.IconButton
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableChipColors
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.ecommerce.R
import com.example.ecommerce.api.model.ProductDetail
import com.example.ecommerce.api.model.ProductVariant
import com.example.ecommerce.api.response.BaseResponse
import com.example.ecommerce.api.response.DetailResponse
import com.example.ecommerce.component.ToastMessage
import com.example.ecommerce.ui.theme.LightGray
import com.example.ecommerce.ui.theme.Purple
import com.example.ecommerce.ui.theme.PurplePink
import com.example.ecommerce.ui.theme.onSurfaceVar
import com.example.ecommerce.ui.theme.poppins
import com.example.ecommerce.ui.theme.textColor
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPagerApi::class)
@Composable
fun DetailScreen(
    navController : NavHostController,
    id:String,
    onReviewClick:(id:String)-> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val detailViewModel: DetailViewModel = hiltViewModel()
    var isSuccess by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var isError by remember { mutableStateOf(false) }
    var result by remember { mutableStateOf(ProductDetail())}
    var totalPrice by remember { mutableStateOf(0) }
    val pagerState = rememberPagerState()

    val isFavorite by detailViewModel.isFavorite.collectAsStateWithLifecycle()

    val uiState by detailViewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }

    uiState.message?.let { message ->
        scope.launch {
            snackBarHostState.showSnackbar(message)
        }
    }

    LaunchedEffect(true){
        detailViewModel.getProductDetail(id)
        detailViewModel.getFavoriteById(id)
    }

    detailViewModel.detailResult.observe(lifecycleOwner){
        when (it) {
            is BaseResponse.Loading -> {
                isLoading = true
                isError = false
            }

            is BaseResponse.Success -> {
                isLoading = false
                isError = false
                isSuccess = true
                result = it.data!!.data
            }

            is BaseResponse.Error -> {
                isLoading = false
                isSuccess = false
                isError = true
            }

            else -> {}
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        },
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text(
                            stringResource(id = R.string.detail),
                            style = MaterialTheme.typography.titleLarge
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.ArrowBack, "back_button")
                        }
                    }
                )
                Divider()
            }
        },
        bottomBar = {
            if(isSuccess) {
                Divider()
                Column(Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            OutlinedButton(
                                modifier = Modifier.fillMaxWidth(),
                                onClick = { },
                            ) {
                                Text(
                                    color = Purple,
                                    text = stringResource(id = R.string.buy_now),
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(modifier = Modifier.weight(1f)
                        ) {
                            Button(
                                modifier = Modifier.fillMaxWidth(),
                                onClick = {
                                    detailViewModel.addProductsToCart(
                                        result,
                                        uiState.productVariant
                                    )
                                },
                                colors = ButtonDefaults.buttonColors(Purple),
                            ) {
                                Text(
                                    text = "+ "+stringResource(id = R.string.cart),
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }
                        }
                    }
                }
            }
        }
    ) {
        Column(modifier = Modifier.padding(it)) {
            if (isSuccess) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    val pages = result.image

                    Box(
                        modifier = Modifier
                            .height(309.dp)
                            .fillMaxWidth()
                            .background(Color.White),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        HorizontalPager(
                            modifier = Modifier,
                            count = 3, state = pagerState,
                            verticalAlignment = Alignment.CenterVertically
                        ) { position ->
                            if (pages!!.isEmpty()) {
                                Image(
                                    modifier = Modifier.fillMaxSize(),
                                    painter = painterResource(id = R.drawable.detail),
                                    contentDescription = "Empty Images"
                                )
                            } else {
                                AsyncImage(
                                    modifier = Modifier.fillMaxSize(),
                                    model = pages[position],
                                    contentDescription = "Images"
                                )
                            }
                        }
                        Column(
                            Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            HorizontalPagerIndicator(
                                modifier = Modifier.padding(bottom = 16.dp),
                                pagerState = pagerState,
                                activeColor = Purple,
                                inactiveColor = LightGray
                            )
                        }
                    }

                    Column(Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Column(
                                Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                horizontalAlignment = Alignment.Start
                            ) {
                                totalPrice = result.productPrice?.plus(uiState.productVariant.variantPrice)!!
                                Text(
                                    text = currency(totalPrice), fontSize = 20.sp,
                                    fontWeight = FontWeight.W600
                                )
                            }
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                horizontalArrangement = Arrangement.End
                            ) {
                                Icon(imageVector = Icons.Default.Share, contentDescription = "Star")
                                Spacer(modifier = Modifier.width(16.dp))

                                Icon(
                                    modifier = Modifier.clickable {
                                        if (isFavorite) {
                                            detailViewModel.setUnFavorite()
                                            detailViewModel.deleteFavoriteById(id)
                                        } else {
                                            detailViewModel.addFavoriteToCart(
                                                result, uiState.productVariant
                                            )
                                        }
                                    },
                                    tint = if (isFavorite) Color.Red else Color.DarkGray,
                                    imageVector = if (isFavorite) {
                                        Icons.Filled.Favorite
                                    } else {
                                        Icons.Default.FavoriteBorder
                                    },
                                    contentDescription = null
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = result.productName.toString(),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W400
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Text(
                                text = stringResource(id = R.string.sold) + " ${result.sale}" ,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.W400
                            )
                            Spacer(modifier = Modifier.width(10.dp))

                            AssistChip(
                                modifier = Modifier.height(21.dp),
                                onClick = {},
                                label = {
                                    Text(
                                        text = "${result.productRating} (${result.totalRating})",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.W400
                                    )
                                },
                                leadingIcon = {
                                    Icon(
                                        tint = Color.Black,
                                        modifier = Modifier.size(15.dp),
                                        imageVector = Icons.Filled.Star,
                                        contentDescription = "Star"
                                    )
                                }
                            )
                        }
                    }

                    Divider()

                    Column(Modifier.padding(16.dp)) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(id = R.string.choose_variant),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.W500
                        )

                        val itemsList: List<ProductVariant> = result.productVariant!!
                        var selectedItem by remember { mutableStateOf(itemsList[0].variantName) }
                        var selectedPrice by remember { mutableStateOf(itemsList[0].variantPrice) }

                        LazyRow(modifier = Modifier.fillMaxWidth()) {
                            items(itemsList) { item ->
                                detailViewModel.setProductDataVariant(selectedItem, selectedPrice)
                                FilterChip(
                                    modifier = Modifier.padding(end = 6.dp),
                                    selected = (selectedItem == item.variantName),
                                    onClick = {
                                        selectedItem = item.variantName
                                        selectedPrice = item.variantPrice
                                        detailViewModel.setProductDataVariant(
                                            selectedItem,
                                            selectedPrice
                                        )
                                    },
                                    label = {
                                        Text(text = item.variantName, style = MaterialTheme.typography.labelLarge)
                                    },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = PurplePink
                                    )
                                )
                            }
                        }
                    }

                    Divider()

                    Column(Modifier.padding(16.dp)) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(id = R.string.description),
                            fontFamily = poppins,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.W500,
                        )

                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp),
                            text = result.description.toString(),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W400
                        )
                    }

                    Divider()

                    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                horizontalArrangement = Arrangement.Start
                            ) {
                                Text(
                                    text = stringResource(id = R.string.buyer_review),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.W500
                                )
                            }
                            Row(horizontalArrangement = Arrangement.End
                            ) {
                                TextButton(onClick = { onReviewClick(result.productId!!) }) {
                                    Text(
                                        text = stringResource(id = R.string.see_all),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.W500
                                    )
                                }
                            }
                        }

                        Row(modifier = Modifier.fillMaxWidth()) {
                            Row(modifier = Modifier,
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.Bottom
                            ) {
                                Icon(imageVector = Icons.Filled.Star, contentDescription = "")
                                Text(
                                    text = result.productRating.toString(),
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.W600
                                )
                                Text(text = "/")
                                Text(
                                    text = "5.0",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.W400
                                )

                            }
                            Spacer(modifier = Modifier.width(16.dp))

                            Column(Modifier.fillMaxWidth()) {
                                Text(
                                    text = "${result.totalSatisfaction}% "+stringResource(id = R.string.buyer_satisfied),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.W600
                                )
                                Text(
                                    text = "${result.totalRating} "+ stringResource(id = R.string.rating) + " · " + "${result.totalReview} "+stringResource(id = R.string.review),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.W400
                                )
                            }
                        }
                    }
                }
            }

            if (isError) {
                ErrorPage(
                    title = stringResource(id = R.string.empty),
                    message = stringResource(id = R.string.resource),
                    button = R.string.refresh,
                    onButtonClick = { isLoading = true },
                    1f
                )
            }

            if (isLoading) {
                Column(modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    CircularProgressIndicator(color = Purple)
                }
            }
        }

    }
}

@Composable
fun ErrorPage(
    title:String,
    message: String,
    button:Int,
    onButtonClick: () -> Unit,
    alpha : Float
){
    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Image(modifier = Modifier.size(128.dp),painter = painterResource(id = R.drawable.smartphone) ,
            contentDescription = "")
        Text(modifier = Modifier.padding(top=5.dp),text = title, fontSize = 32.sp,
            fontWeight = FontWeight.W500)
        Text(text = message,fontSize = 16.sp, fontWeight = FontWeight.W400)
        Button(modifier = Modifier
            .padding(top = 5.dp)
            .alpha(alpha),
            onClick = { onButtonClick() },
            colors = ButtonDefaults.buttonColors(Purple)
        ) {
            Text(
                text = stringResource(id = button),
                fontWeight = FontWeight.W500
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun DetailPreview(){
    Column (Modifier.background(Color.White)) {
        DetailScreen(rememberNavController(),id = "1"){
            it
        }
    }
}

@Composable
fun FavoriteButton(isFavorite : Boolean) {
        var isFavorite by remember { mutableStateOf(false) }
        Icon(modifier = Modifier.clickable {
                isFavorite = !isFavorite
            },
            tint = if(isFavorite) Color.Red else Color.DarkGray,
            imageVector = if (isFavorite) {
                Icons.Filled.Favorite
            } else {
                Icons.Default.FavoriteBorder
            },
            contentDescription = null
        )
}

fun currency (price: Int) : String{
    val localId = Locale("in","ID")
    val currencyFormat = NumberFormat.getCurrencyInstance(localId)
    return currencyFormat.format(price)
}