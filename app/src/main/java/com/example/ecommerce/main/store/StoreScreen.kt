package com.example.ecommerce.main.store

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FormatListBulleted
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.bumptech.glide.load.HttpException
import com.example.ecommerce.R
import com.example.ecommerce.api.model.Product
import com.example.ecommerce.api.request.AuthRequest
import com.example.ecommerce.api.response.BaseResponse
import com.example.ecommerce.component.ProgressDialog
import com.example.ecommerce.component.ToastMessage
import com.example.ecommerce.ui.theme.Purple
import com.example.ecommerce.ui.theme.textColor
import com.example.ecommerce.util.Constant
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.Locale
import java.util.concurrent.TimeoutException

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreScreen2() {
    val lifecycleOwner = LocalLifecycleOwner.current

    var isLoading by rememberSaveable { mutableStateOf(false)}
    if (isLoading) ProgressDialog().ProgressDialog()
    var search by rememberSaveable { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }

    var searchProduct : ArrayList<String> = arrayListOf()

    val productViewModel : StoreViewModel = hiltViewModel()
    val products = if(search.isEmpty()) productViewModel.getProducts("").collectAsLazyPagingItems() else
        productViewModel.getProducts(search).collectAsLazyPagingItems()
    val productsFilter = productViewModel.getProductsFilter("","lenovo",10000000,20000000,"sale").collectAsLazyPagingItems()

    productViewModel.searchResult.observe(lifecycleOwner){
        when (it) {
            is BaseResponse.Loading -> {
                isLoading = true
            }
            is BaseResponse.Success -> {
                isLoading = false
                searchProduct = it.data!!.data
            }
            else -> {}
        }
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 16.dp)){
        SearchBar(
            modifier = Modifier.fillMaxWidth(),
            query = search,
            colors = SearchBarDefaults.colors(containerColor = Color.White),
            tonalElevation = 0.dp,
            onQueryChange = {
                search = it
                productViewModel.searchProductList(search)
            },
            onSearch = {
                active = false
            },
            active = active,
            onActiveChange = {
                active = it
            },
            placeholder = {
                Text(text = stringResource(id = R.string.search),
                    fontWeight = FontWeight.W400,
                    fontSize = 16.sp,
                    style = MaterialTheme.typography.bodyMedium)
            },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
            },
            trailingIcon = {
                Icon(
                    modifier = Modifier.clickable {
                        if(search.isNotEmpty()) {
                            search = ""
                        }else{
                            active = false
                        }
                    },
                    imageVector = Icons.Default.Close,
                    contentDescription = "close")
            }
        ) {
            val filteredList = productViewModel.filterSearch(search,searchProduct)
            filteredList.forEach { item ->
                ListItem(
                    modifier = Modifier.clickable {
                        search = item
                        active = false
                    },
                    headlineContent = {
                        Text(text = item, fontSize = 12.sp, fontWeight = FontWeight.W400)
                    },
                    leadingContent = {
                        Icon(modifier = Modifier.size(18.dp),
                            imageVector = Icons.Default.Search,
                            contentDescription = null)
                    },
                    trailingContent = {
                        Icon(modifier = Modifier.size(18.dp),
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = null)
                    }
                )
            }
        }
        StoreContent(products)
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun StoreContent(products : LazyPagingItems<Product>){
    var isClickedGrid by rememberSaveable { mutableStateOf(false)}
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Row(
        Modifier
            .fillMaxWidth()
            .padding(top = 8.dp), verticalAlignment = Alignment.CenterVertically){
        val itemsCategory = listOf("Apple", "Asus", "Dell", "Lenovo")
        var selectedItemCategory by remember { mutableStateOf("")}

        val itemsList = listOf("Ulasan", "Penjualan", "Harga Terendah", "Harga Tertinggi")
        var selectedItemList by remember { mutableStateOf("")}

        val lowestPrice = rememberSaveable { mutableStateOf("") }
        val highestPrice = rememberSaveable { mutableStateOf("") }

        val itemsListFilter : ArrayList<String> = arrayListOf()

        if(showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                sheetState = sheetState
            ) {
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 24.dp)) {
                    Row (Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically){
                        Column(Modifier.weight(1f), horizontalAlignment = Alignment.Start
                        ) {
                            Text(text = stringResource(id = R.string.filter),fontSize = 16.sp, fontWeight = FontWeight.W700)
                        }
                        Column(Modifier.weight(1f), horizontalAlignment = Alignment.End
                        ) {
                            TextButton(onClick = {
                                scope.launch {
                                    sheetState.hide()
                                }.invokeOnCompletion {
                                    if(!sheetState.isVisible)
                                        showBottomSheet=false
                                }
                            }) {
                                Text(text = stringResource(id = R.string.reset),fontSize = 14.sp, color = Purple)
                            }
                        }
                    }

                    Text(text = stringResource(id = R.string.sort),fontSize = 14.sp, fontWeight = FontWeight.W600)
                    FlowRow(modifier = Modifier.fillMaxWidth()) {
                        itemsList.forEach {item->
                            FilterChip(
                                modifier = Modifier.padding(end = 6.dp),
                                selected = (item==selectedItemList),
                                onClick = { selectedItemList = item },
                                label = {
                                    Text(text = item)
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(stringResource(id = R.string.category),fontSize = 14.sp, fontWeight = FontWeight.W600)
                    LazyRow(modifier = Modifier.fillMaxWidth()) {
                        items(itemsCategory) {item->
                            FilterChip(
                                modifier = Modifier.padding(end = 6.dp),
                                selected = (item==selectedItemCategory),
                                onClick = { selectedItemCategory = item },
                                label = {
                                    Text(text = item)
                                }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(stringResource(id = R.string.price),fontSize = 14.sp, fontWeight = FontWeight.W600)
                    Row(modifier = Modifier.padding(vertical = 8.dp)){
                        Column(modifier = Modifier
                            .weight(1f)
                            .padding(end = 10.dp)) {
                            TextFieldPrice(label = R.string.lowest, input = lowestPrice )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            TextFieldPrice(label = R.string.highest, input = highestPrice)
                        }
                    }

                    Button(onClick = {
                        scope.launch {
                            sheetState.hide()
                        }.invokeOnCompletion {
                            if(!sheetState.isVisible)
                                showBottomSheet=false
                        }
                    },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        colors = ButtonDefaults.buttonColors(Purple)
                    ) {
                        Text(
                            text = stringResource(id = R.string.tampilkan),
                            fontWeight = FontWeight.W500
                        )
                    }
                }
            }
        }

        Row (Modifier.weight(1f)){
            AssistChip(modifier = Modifier.padding(end = 6.dp),
                onClick = {  showBottomSheet = true },
                label = {
                    Text(text = stringResource(id = R.string.filter))
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Tune,
                        contentDescription = null,
                        modifier = Modifier.size(AssistChipDefaults.IconSize)
                    )
                }
            )

            var selectedItemFilter by remember { mutableStateOf("")}
            LazyRow(modifier = Modifier) {
                items(itemsListFilter) {item->
                    if(item.isEmpty()){
                        null
                    }else {
                        AssistChip(
                            modifier = Modifier.padding(end = 6.dp),
                            onClick = { selectedItemFilter = item },
                            label = {
                                Text(text = item)
                            }
                        )
                    }
                }
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End) {
            Spacer(modifier = Modifier
                .height(24.dp)
                .width(1.dp)
                .background(Color.Gray))
            Spacer(modifier = Modifier.width(10.dp))
            Icon(
                modifier = Modifier.clickable {
                    isClickedGrid =! isClickedGrid
                },
                imageVector = if(isClickedGrid) Icons.Default.GridView else Icons.Default.FormatListBulleted,
                contentDescription = "List"
            )
        }
    }

    Column {
        StoreProductList(isClickedGrid, products = products)
    }
}

@Composable
fun StoreErrorPage(title:String,
                   message: String,
                   button:Int,
                   onButtonClick: () -> Unit
){
    Column(modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Image(modifier = Modifier.size(128.dp),painter = painterResource(id = R.drawable.smartphone) ,
            contentDescription = "")
        Text(modifier = Modifier.padding(top=5.dp),text = title, fontSize = 32.sp,
            fontWeight = FontWeight.W500)
        Text(text = message,fontSize = 16.sp,
            fontWeight = FontWeight.W400)
        Button(onClick = { onButtonClick() },
            colors = ButtonDefaults.buttonColors(Purple),
            enabled = true
        ) {
            Text(
                text = stringResource(id = button),
                fontWeight = FontWeight.W500
            )
        }
    }
}

@Composable
fun StoreProductList(
    isClickedGrid: Boolean,
    products : LazyPagingItems<Product>
){
    when (val state = products.loadState.refresh) {
        is LoadState.Error -> {
            when (state.error) {
                is HttpException -> {
                    StoreErrorPage(
                        title = "Empty",
                        message = "Your requested data is unavailable",
                        button = R.string.reset,
                        onButtonClick = {}
                    )
                }

                is IOException -> {
                    StoreErrorPage(
                        title = "Connection",
                        message = "Your connection is unavailable",
                        button = R.string.refresh,
                        onButtonClick = { products.refresh()}
                    )
                }

                is TimeoutException-> {
                    StoreErrorPage(
                        title = "Time Out",
                        message = "Time Out",
                        button = R.string.refresh,
                        onButtonClick = { products.refresh()}
                    )
                }

                else -> {
                    StoreErrorPage(title = "500",
                        message = "Internal Server Error",
                        button = R.string.refresh,
                        onButtonClick = { products.refresh()}
                    )
                }
            }
        }

        is LoadState.Loading -> {
            if(isClickedGrid){
                LazyVerticalGrid(GridCells.Fixed(2)) {
                    repeat(10){
                        item {  AnimatedGridShimmer() }
                    }
                }
            }else {
                repeat(7) {
                    AnimatedListShimmer()
                }
            }
        }
        else -> {}
    }

    if(isClickedGrid){
        LazyVerticalGrid(GridCells.Fixed(2)) {
            items(count = products.itemCount,
                key = products.itemKey { it.productId }
            ) { index ->
                val item = products[index]
                CardGrid(product = item)
            }

            when (products.loadState.append) {
                is LoadState.Loading -> {
                    item {
                        Column(modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Bottom,
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }

                else -> {}
            }
        }
    }else {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(count = products.itemCount,
                key = products.itemKey { it.productId }
            ) { index ->
                val item = products[index]
                CardList(product = item)
            }

            when (products.loadState.append) { // Pagination
                is LoadState.Loading -> { // Pagination Loading UI
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Bottom,
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }

                else -> {}
            }
        }
    }
}

@Composable
fun CardList(product: Product?){
    Column(Modifier.padding(vertical = 5.dp)) {
        Card(modifier = Modifier
            .fillMaxWidth()
            .clickable {
            }, shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(3.dp)
        ) {
            Box(modifier = Modifier.background(Color.White)) {
                Row(
                    modifier = Modifier.padding(10.dp)
                ) {
                    Card(
                        modifier = Modifier.size(80.dp),
                        shape = RoundedCornerShape(5.dp)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            if(product!!.image.isEmpty()){
                                Image(
                                    painter = painterResource(id = R.drawable.thumbnail),
                                    contentDescription = "image"
                                )
                            }else {
                                AsyncImage(
                                    model = product.image,
                                    contentDescription = "Products image"
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
                            text = product!!.productName,
                            fontWeight = FontWeight.W400,
                            fontSize = 12.sp,
                            lineHeight = 15.sp
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(
                            text = "Rp${product.productPrice}",
                            fontWeight = FontWeight.W600,
                            fontSize = 14.sp
                        )
                        Row(Modifier.padding(top = 5.dp),
                            verticalAlignment = Alignment.CenterVertically){
                            Icon(
                                modifier = Modifier.size(12.dp),
                                imageVector = Icons.Filled.AccountCircle,
                                contentDescription = "Account"
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(product.store,
                                fontWeight = FontWeight.W400,
                                fontSize = 10.sp)
                        }

                        Row(Modifier.padding(top = 2.dp),
                            verticalAlignment = Alignment.CenterVertically){
                            Icon(
                                modifier = Modifier.size(12.dp),
                                imageVector = Icons.Filled.Star,
                                contentDescription = "Star"
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text("${product.productRating} | Terjual ${product.sale}",
                                fontWeight = FontWeight.W400,
                                fontSize = 10.sp)
                        }
                    }

                }
            }
        }
    }
}

@Composable
fun AnimatedListShimmer() {
    val shimmerColors = listOf(
        Color.LightGray.copy(alpha = 0.6f),
        Color.LightGray.copy(alpha = 0.2f),
        Color.LightGray.copy(alpha = 0.6f),
    )

    val transition = rememberInfiniteTransition(label = "")
    val translateAnim = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset.Zero,
        end = Offset(x = translateAnim.value, y = translateAnim.value)
    )

    ShimmerCardList(brush = brush)
}

@Composable
fun ShimmerCardList(brush: Brush){
    Column(Modifier.padding(vertical = 5.dp)) {
        Card(modifier = Modifier
            .fillMaxWidth()
            .clickable {
            }, shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(3.dp)
        ) {
            Box(modifier = Modifier.background(Color.White)) {
                Row(modifier = Modifier.padding(10.dp)
                ) {
                    Spacer(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(brush)
                    )
                    Spacer(modifier = Modifier.width(8.dp))

                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp)
                    ) {

                        Spacer(
                            modifier = Modifier
                                .height(20.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .fillMaxWidth(fraction = 1f)
                                .background(brush)
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        Spacer(
                            modifier = Modifier
                                .height(20.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .fillMaxWidth(fraction = 1f)
                                .background(brush)
                        )
                        Row(Modifier.padding(top = 5.dp),
                            verticalAlignment = Alignment.CenterVertically){
                            Spacer(
                                modifier = Modifier
                                    .size(20.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(brush)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Spacer(
                                modifier = Modifier
                                    .height(20.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .fillMaxWidth(fraction = 1f)
                                    .background(brush)
                            )
                        }

                        Row(Modifier.padding(top = 5.dp),
                            verticalAlignment = Alignment.CenterVertically){
                            Spacer(
                                modifier = Modifier
                                    .size(20.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(brush)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Spacer(
                                modifier = Modifier
                                    .height(20.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .fillMaxWidth(fraction = 1f)
                                    .background(brush)
                            )
                        }
                    }

                }
            }
        }
    }
}

@Composable
fun CardGrid(product: Product?){
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
                        if(product!!.image.isEmpty()){
                            Image(
                                painter = painterResource(id = R.drawable.thumbnail),
                                contentDescription = "image"
                            )
                        }else {
                            AsyncImage(
                                model = product.image,
                                contentDescription = "Products image"
                            )
                        }
                    }
                }

                Column(modifier = Modifier.padding(10.dp)) {
                    Text(
                        text = product!!.productName,
                        fontWeight = FontWeight.W400,
                        fontSize = 12.sp,
                        maxLines = 2,
                        lineHeight = 15.sp
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = "Rp${product.productPrice}",
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
                        Text(product.store,
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
                            "${product.productRating} | Terjual ${product.sale}",
                            fontWeight = FontWeight.W400,
                            fontSize = 10.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AnimatedGridShimmer(){
    val shimmerColors = listOf(
        Color.LightGray.copy(alpha = 0.6f),
        Color.LightGray.copy(alpha = 0.2f),
        Color.LightGray.copy(alpha = 0.6f),
    )

    val transition = rememberInfiniteTransition(label = "")
    val translateAnim = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset.Zero,
        end = Offset(x = translateAnim.value, y = translateAnim.value)
    )
    ShimmerGridList(brush = brush)
}

@Composable
fun ShimmerGridList(brush: Brush){
    Column(Modifier.padding(top = 5.dp, bottom = 5.dp, end = 5.dp)) {
        Card (modifier = Modifier
            .width(186.dp)
            .clickable {},
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(3.dp)
        ){
            Column(Modifier.background(Color.White)) {
                Spacer(
                    modifier = Modifier
                        .size(186.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .fillMaxWidth(fraction = 1f)
                        .background(brush)
                )

                Column(modifier = Modifier.padding(10.dp)) {
                    Spacer(
                        modifier = Modifier
                            .height(20.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .fillMaxWidth(fraction = 1f)
                            .background(brush)
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Spacer(
                        modifier = Modifier
                            .height(20.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .fillMaxWidth(fraction = 1f)
                            .background(brush)
                    )

                    Row(
                        Modifier.padding(top = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(
                            modifier = Modifier
                                .size(20.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(brush)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Spacer(
                            modifier = Modifier
                                .height(20.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .fillMaxWidth(fraction = 1f)
                                .background(brush)
                        )
                    }

                    Row(
                        Modifier.padding(top = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(
                            modifier = Modifier
                                .size(20.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(brush)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Spacer(
                            modifier = Modifier
                                .height(20.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .fillMaxWidth(fraction = 1f)
                                .background(brush)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TextFieldPrice(
    label : Int,
    input : MutableState<String>,
    imeAction: ImeAction = ImeAction.Done,
) {
    val localFocusManager = LocalFocusManager.current
    OutlinedTextField(
        value = input.value,
        onValueChange = {
            input.value = it
        },
        label = {
            Text(
                text = stringResource(id = label),
                style = MaterialTheme.typography.bodyMedium,
            )
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
fun StoreScreenPreview() {
}