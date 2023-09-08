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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import coil.compose.AsyncImage
import com.example.ecommerce.R
import com.example.ecommerce.api.model.ProductVariant
import com.example.ecommerce.api.response.BaseResponse
import com.example.ecommerce.api.response.DetailResponse
import com.example.ecommerce.component.ToastMessage
import com.example.ecommerce.ui.theme.LightGray
import com.example.ecommerce.ui.theme.Purple
import com.example.ecommerce.ui.theme.textColor
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState

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
    var result by remember { mutableStateOf(DetailResponse.ProductDetail())}
    var price by remember { mutableStateOf(0) }
    var totalPrice by remember { mutableStateOf(0) }
    val pagerState = rememberPagerState()

    val data by detailViewModel.uiState.collectAsState()
    val isFavorite by detailViewModel.isFavorite.collectAsStateWithLifecycle()
    Log.d("isFavorite", isFavorite.toString())

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

    val uiState by detailViewModel.uiState.collectAsState()
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    val context = LocalContext.current

    val productLocal by detailViewModel.productLocal.collectAsState()

    uiState.message?.let { message ->
        ToastMessage().showMsg(context,message)
    }

    Scaffold(
        topBar = {
            TopAppBar(modifier = Modifier.drawBehind {
                val borderSize = 1.dp.toPx()
                drawLine(
                    color = LightGray,
                    start = Offset(0f,size.height),
                    end = Offset(size.width,size.height),
                    strokeWidth = borderSize
                )
            },
                title = {
                    Text(
                        stringResource(id = R.string.detail),
                        fontSize = 22.sp, color = textColor,
                        fontWeight = FontWeight.Normal)
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack,"back_button")
                    }
                }
            )
        },
    ) {
        Column(modifier = Modifier.padding(it)) {
            if (isSuccess)
                Column(modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                ) {
                    val pages = result.image

                    Box(modifier = Modifier
                        .height(309.dp)
                        .fillMaxWidth()
                        .background(Color.White),
                        contentAlignment = Alignment.BottomCenter) {
                        HorizontalPager(
                            modifier = Modifier,
                            count = 3, state = pagerState,
                            verticalAlignment = Alignment.CenterVertically
                        ) { position ->
                            if(pages!!.isEmpty()) {
                                Image(
                                    modifier = Modifier.fillMaxSize(),
                                    painter = painterResource(id = R.drawable.detail),
                                    contentDescription = "Empty Images")
                            }else{
                                AsyncImage(
                                    modifier = Modifier.fillMaxSize(),
                                    model = pages[position],
                                    contentDescription = "Images")
                            }
                        }
                        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            HorizontalPagerIndicator(
                                modifier = Modifier.padding(bottom = 16.dp),
                                pagerState = pagerState,
                                activeColor = Purple,
                                inactiveColor = LightGray)
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
                                totalPrice = result.productPrice?.plus(price)!!
                                Text(
                                    text = "Rp${totalPrice}", fontSize = 20.sp,
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

                                Icon(modifier = Modifier.clickable {
                                    //isFavorite = !isFavorite
                                    if(isFavorite){
                                        detailViewModel.deleteFavoriteById(id)
                                    }else {
                                        detailViewModel.addFavoriteToCart(
                                            result,
                                            uiState.productVariant
                                        )
                                    }
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
                                text = "Terjual ${result.sale}",
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
                            text = "Pilih Varian",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.W500
                        )

                        val itemsList: List<ProductVariant> = result.productVariant!!
                        var selectedItem by remember { mutableStateOf(itemsList[0].variantName) }
                        LazyRow(modifier = Modifier.fillMaxWidth()) {
                            items(itemsList) {item->
                               FilterChip(
                                    modifier = Modifier.padding(end = 6.dp),
                                    selected = (item.variantName == selectedItem),
                                    onClick = {
                                        selectedItem = item.variantName
                                        price = item.variantPrice
                                        detailViewModel.setProductDataVariant(item.variantName,price)
                                    },
                                    label = {
                                        Text(text = item.variantName)
                                    }
                                )
                            }
                        }
                    }

                    Divider()

                    Column(Modifier.padding(16.dp)) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = "Deskripsi produk",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.W500
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

                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Column(
                                Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                horizontalAlignment = Alignment.Start
                            ) {
                                Text(
                                    text = "Ulasan Pembeli",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.W500
                                )
                            }
                            Column(
                                Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                horizontalAlignment = Alignment.End
                            ) {
                                TextButton(onClick = { onReviewClick(result.productId!!) }) {
                                    Text(
                                        text = "Lihat semua",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.W500
                                    )
                                }
                            }
                        }

                        Row(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier,
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
                                    text = "${result.totalSatisfaction}% pembeli merasa puas",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.W600
                                )
                                Text(
                                    text = "${result.totalRating} rating · ${result.totalReview} ulasan",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.W400
                                )
                            }
                        }

                    }

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
                                        text = stringResource(id = R.string.buy),
                                        fontWeight = FontWeight.W500
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Button(
                                    modifier = Modifier.fillMaxWidth(),
                                    onClick = {
                                        detailViewModel.addProductsToCart(result, data.productVariant)
                                    },
                                    colors = ButtonDefaults.buttonColors(Purple),
                                ) {
                                    Text(
                                        text = "+ Keranjang",
                                        fontWeight = FontWeight.W500
                                    )
                                }
                            }
                        }
                    }
                }

            if (isError)
                ErrorPage(
                    title = "Empty",
                    message = "Your requested data is unavailable",
                    button = R.string.refresh,
                    onButtonClick = { isLoading = true },
                    1f
                )

            if (isLoading)
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    CircularProgressIndicator(color = Purple)
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
        Text(modifier = Modifier.padding(top=5.dp),text = message,fontSize = 16.sp,
            fontWeight = FontWeight.W400)
        Button(modifier = Modifier
            .padding(top = 2.dp)
            .alpha(alpha),onClick = { onButtonClick() },
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
        //DetailScreen(id = "1",{rememberNavController().popBackStack()})
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

