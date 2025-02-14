package com.example.ecommerce.main.store

import android.content.res.Configuration
import android.util.Log
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.FormatListBulleted
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil.compose.AsyncImage
import com.example.core.api.model.Product
import com.example.core.api.model.ProductFilter
import com.example.core.api.response.BaseResponse
import com.example.ecommerce.R
import com.example.ecommerce.component.ToastMessage
import com.example.ecommerce.main.data.mockProducts
import com.example.ecommerce.main.detail.currency
import com.example.ecommerce.ui.theme.CardBorder
import com.example.ecommerce.ui.theme.EcommerceTheme
import com.example.ecommerce.ui.theme.Purple
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.SocketTimeoutException

@ExperimentalMaterialApi
@ExperimentalMaterial3Api
@ExperimentalLayoutApi
@Composable
fun StoreScreen(
    onDetailClick: (id: String) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val storeViewModel: StoreViewModel = hiltViewModel()
    var search by rememberSaveable { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    val products = storeViewModel.getProductsFilter.collectAsLazyPagingItems()
    val isClickedGrid by rememberSaveable { mutableStateOf(false) }

    val searchData = storeViewModel.searchData.collectAsState()
    storeViewModel.searchQuery(searchData.value)

    storeViewModel.tokenResult.observe(lifecycleOwner) {
        when (it) {
            is BaseResponse.Success -> {
                ToastMessage().showMsg(context, it.data!!.message)
            }

            is BaseResponse.Error -> {
                ToastMessage().showMsg(context, it.msg.toString())
            }

            else -> {}
        }
    }

//    SearchDialog(
//        openDialog = showDialog,
//        onCloseDialog = {
//            showDialog = false
//        },
//        searchData.value
//    )
//
//    Column(
//        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)
//    ) {
//        OutlinedTextField(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(top = 16.dp)
//                .clickable { showDialog = true },
//            placeholder = {
//                Text(
//                    text = stringResource(id = R.string.search),
//                    style = MaterialTheme.typography.bodyMedium,
//                )
//            },
//            singleLine = true,
//            maxLines = 1,
//            value = searchData.value,
//            onValueChange = {
//                search = it
//            },
//            leadingIcon = {
//                Icon(
//                    imageVector = Icons.Default.Search,
//                    contentDescription = "Search"
//                )
//            },
//            enabled = false
//        )
//
//        when (val state = products.loadState.refresh) {
//            is LoadState.Error -> {
//                var error = state.error
//                if (error is HttpException) {
//                    when (error.code()) {
//                        404 -> {
//                            ErrorPage(
//                                title = stringResource(id = R.string.empty),
//                                message = stringResource(id = R.string.resource),
//                                button = R.string.refresh,
//                                onButtonClick = {
//                                    products.refresh()
//                                    storeViewModel.resetQuery()
//                                    storeViewModel.buttonAnalytics("Refresh")
//                                },
//                                1f
//                            )
//                        }
//
//                        500 -> {
//                            ErrorPage(
//                                title = error.code().toString(),
//                                message = stringResource(id = R.string.internal),
//                                button = R.string.refresh,
//                                onButtonClick = {
//                                    products.refresh()
//                                    storeViewModel.resetQuery()
//                                    storeViewModel.buttonAnalytics("Refresh")
//                                },
//                                1f
//                            )
//                        }
//
//                        401 -> {
//                            ErrorPage(
//                                title = stringResource(id = R.string.connection),
//                                message = stringResource(id = R.string.connection_unavailable),
//                                button = R.string.refresh,
//                                onButtonClick = {
//                                    // products.refresh()
//                                    // storeViewModel.resetQuery()
//                                    storeViewModel.buttonAnalytics("Refresh")
//                                    storeViewModel.refreshToken()
//                                },
//                                alpha = 1f
//                            )
//                        }
//                    }
//                } else if (error is SocketTimeoutException) {
//                    ErrorPage(
//                        title = stringResource(id = R.string.connection),
//                        message = stringResource(id = R.string.connection_unavailable),
//                        button = R.string.refresh,
//                        onButtonClick = {
//                            storeViewModel.refreshToken()
//                            storeViewModel.buttonAnalytics("Refresh")
//                        },
//                        alpha = 1f
//                    )
//                }
//            }
//
//            is LoadState.Loading -> {
//                if (isClickedGrid) {
//                    AnimatedFilter()
//                    LazyVerticalGrid(GridCells.Fixed(2)) {
//                        repeat(10) {
//                            item { AnimatedGridShimmer() }
//                        }
//                    }
//                } else {
//                    AnimatedFilter()
//                    repeat(7) {
//                        AnimatedListShimmer()
//                    }
//                }
//            }
//
//            else -> {
//                StoreListContent(
//                    products = products,
//                    //storeViewModel = storeViewModel,
//                    filter = storeViewModel.filter.value,
//                    onDetailClick = onDetailClick,
//                    onItemAnalytics = { productId ->
//                        storeViewModel.selectItemProducts(productId)
//                    },
//                    onButtonResetAnalytics = {
//                        storeViewModel.buttonAnalytics("Reset Button")
//                    },
//                    setSearchText = {
//                        storeViewModel.setSearchText("")
//                    },
//                    setQuery = { brand,low, high, sort ->
//                        storeViewModel.setQuery(brand, low, high, sort)
//                    },
//                    filterAnalytics = { dataFilter ->
//                        storeViewModel.filterAnalytics(dataFilter)
//                    }
//                )
//                storeViewModel.viewListAnalytics(products)
//            }
//        }
//    }

    StoreContent(
        searchData = searchData.value,
        products = products,
        resetQuery = { storeViewModel.resetQuery() },
        onButtonRefreshAnalytics = { storeViewModel.buttonAnalytics("Refresh") },
        refreshToken = { storeViewModel.refreshToken() },
        isClickedGrid = isClickedGrid,
        filter = storeViewModel.filter.value,
        onDetailClick = onDetailClick,
        onItemAnalytics = { productId ->
            storeViewModel.selectItemProducts(productId)
        },
        onButtonResetAnalytics = {
            storeViewModel.buttonAnalytics("Reset")
        },
        setSearchText = {
            storeViewModel.setSearchText("")
        },
        setQuery = { brand,low, high, sort ->
            storeViewModel.setQuery(brand, low, high, sort)
        },
        filterAnalytics = { dataFilter ->
            storeViewModel.filterAnalytics(dataFilter)
        },
         viewListAnalytics = {
             storeViewModel.viewListAnalytics(products)
         }
    )
}

@ExperimentalMaterialApi
@ExperimentalMaterial3Api
@ExperimentalLayoutApi
@Composable
fun StoreContent(
    searchData: String,
    products: LazyPagingItems<Product>,
    resetQuery: () -> Unit,
    onButtonRefreshAnalytics: () -> Unit,
    refreshToken: () -> Unit,
    isClickedGrid : Boolean,
    filter : ProductFilter,
    onDetailClick: (id: String) -> Unit,
    onItemAnalytics: (id: String) -> Unit,
    onButtonResetAnalytics: () -> Unit,
    setSearchText: () -> Unit,
    setQuery: (brand:String?, low:Int?, high: Int?, sort: String?) -> Unit,
    filterAnalytics: (dataFilter: List<String>) -> Unit,
    viewListAnalytics: () -> Unit
){
    var search by rememberSaveable { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var isClickedGrid by rememberSaveable { mutableStateOf(isClickedGrid) }

    SearchDialog(
        openDialog = showDialog,
        onCloseDialog = {
            showDialog = false
        },
        searchData
    )

    Column (modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)){
        Column (modifier = Modifier.padding(horizontal = 16.dp)){
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .clickable { showDialog = true },
                colors = OutlinedTextFieldDefaults.colors(
                    disabledBorderColor = MaterialTheme.colorScheme.outline
                ),
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.search),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                },
                singleLine = true,
                maxLines = 1,
                value = searchData,
                onValueChange = {
                    search = it
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search"
                    )
                },
                enabled = false
            )

            when (val state = products.loadState.refresh) {
                is LoadState.Error -> {
                    val error = state.error
                    if (error is HttpException) {
                        when (error.code()) {
                            404 -> {
                                ErrorPage(
                                    title = stringResource(id = R.string.empty),
                                    message = stringResource(id = R.string.resource),
                                    button = R.string.refresh,
                                    onButtonClick = {
                                        products.refresh()
//                                    storeViewModel.resetQuery()
//                                    storeViewModel.buttonAnalytics("Refresh")
                                        resetQuery()
                                        onButtonRefreshAnalytics()
                                    },
                                    1f
                                )
                            }

                            500 -> {
                                ErrorPage(
                                    title = error.code().toString(),
                                    message = stringResource(id = R.string.internal),
                                    button = R.string.refresh,
                                    onButtonClick = {
                                        products.refresh()
                                        //storeViewModel.resetQuery()
                                        //storeViewModel.buttonAnalytics("Refresh")
                                        resetQuery()
                                        onButtonRefreshAnalytics()
                                    },
                                    1f
                                )
                            }

                            401 -> {
                                ErrorPage(
                                    title = stringResource(id = R.string.connection),
                                    message = stringResource(id = R.string.connection_unavailable),
                                    button = R.string.refresh,
                                    onButtonClick = {
                                        //storeViewModel.buttonAnalytics("Refresh")
                                        //storeViewModel.refreshToken()
                                        onButtonRefreshAnalytics()
                                        refreshToken()
                                    },
                                    alpha = 1f
                                )
                            }
                        }
                    } else if (error is SocketTimeoutException) {
                        ErrorPage(
                            title = stringResource(id = R.string.connection),
                            message = stringResource(id = R.string.connection_unavailable),
                            button = R.string.refresh,
                            onButtonClick = {
//                            storeViewModel.refreshToken()
//                            storeViewModel.buttonAnalytics("Refresh")
                                refreshToken()
                                onButtonRefreshAnalytics()
                            },
                            alpha = 1f
                        )
                    }
                }

                is LoadState.Loading -> {
                    if (isClickedGrid) {
                        AnimatedFilter()
                        LazyVerticalGrid(GridCells.Fixed(2)) {
                            repeat(10) {
                                item { AnimatedGridShimmer() }
                            }
                        }
                    } else {
                        AnimatedFilter()
                        repeat(7) {
                            AnimatedListShimmer()
                        }
                    }
                }

                else -> {
                    StoreListContent(
                        products = products,
                        //storeViewModel = storeViewModel,
                        filter = filter,
                        onDetailClick = onDetailClick,
                        onItemAnalytics = { productId ->
                            onItemAnalytics(productId)
                        },
                        onButtonResetAnalytics = { onButtonResetAnalytics() },
                        setSearchText = { setSearchText() },
                        setQuery = { brand, low, high, sort ->
                            setQuery(brand, low, high, sort)
                        },
                        filterAnalytics = { dataFilter ->
                            filterAnalytics(dataFilter)
                        }
                    )
                    viewListAnalytics()
                }
            }
        }
    }
}

@ExperimentalMaterialApi
@ExperimentalMaterial3Api
@ExperimentalLayoutApi
@Composable
fun StoreListContent(
    products: LazyPagingItems<Product>,
    filter : ProductFilter,
    onDetailClick: (id: String) -> Unit,
    onItemAnalytics: (id: String) -> Unit,
    
    onButtonResetAnalytics: () -> Unit,
    setSearchText: () -> Unit,
    setQuery: (brand:String?, low:Int?, high: Int?, sort: String?) -> Unit,
    filterAnalytics: (dataFilter: List<String>) -> Unit
) {
    var isClickedGrid by rememberSaveable { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    //val filter = storeViewModel.filter.value
    val selectedCategory = filter.brand ?: ""
    val selectedList = filter.sort ?: ""
    val lowest = if (filter.lowest == null) "" else "${filter.lowest}"
    val highest = if (filter.highest == null) "" else "${filter.highest}"
    val dataFilter: List<String> = listOf(selectedCategory, lowest, highest, selectedList)


    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val itemsCategory = listOf("Apple", "Asus", "Dell", "Lenovo")
        var selectedItemCategory by rememberSaveable { mutableStateOf(selectedCategory) }
        Log.d("SelectedItemCategory", selectedItemCategory)

        val itemsList = listOf("Ulasan", "Penjualan", "Harga Terendah", "Harga Tertinggi")
        var selectedItemList by rememberSaveable { mutableStateOf(selectedList) }
        Log.d("SelectedItemList", selectedItemList)

        val lowestPrice = rememberSaveable { mutableStateOf(lowest) }
        val highestPrice = rememberSaveable { mutableStateOf(highest) }

        Row(Modifier.weight(1f)) {
            AssistChip(
                modifier = Modifier.padding(end = 6.dp),
                onClick = { showBottomSheet = true },
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

            var selectedItemFilter by remember { mutableStateOf("") }
            LazyRow(modifier = Modifier) {
                items(dataFilter) { item ->
                    if (item.isNotEmpty()) {
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

        Row(
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
                } else Icons.AutoMirrored.Filled.FormatListBulleted,
                contentDescription = "List",
                tint = MaterialTheme.colorScheme.surface
            )
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                sheetState = sheetState,
                containerColor = MaterialTheme.colorScheme.background
            ) {
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 24.dp)
                ) {
                    Row(
                        Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            Modifier.weight(1f),
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = stringResource(id = R.string.filter),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.W700
                            )
                        }
                        Column(
                            Modifier.weight(1f),
                            horizontalAlignment = Alignment.End
                        ) {
                            TextButton(onClick = {
                                selectedItemCategory = ""
                                selectedItemList = ""
                                lowestPrice.value = ""
                                highestPrice.value = ""
                                //storeViewModel.buttonAnalytics("Reset Button")
                                //storeViewModel.setSearchText("")
                                onButtonResetAnalytics()
                                setSearchText()
                            }) {
                                Text(
                                    text = stringResource(id = R.string.reset),
                                    fontSize = 14.sp,
                                    color = Purple
                                )
                            }
                        }
                    }

                    Text(
                        text = stringResource(id = R.string.sort),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W600
                    )
                    FlowRow(modifier = Modifier.fillMaxWidth()) {
                        itemsList.forEach { item ->
                            FilterChip(
                                modifier = Modifier.padding(end = 6.dp),
                                selected = (item == selectedItemList),
                                onClick = { selectedItemList = item },
                                label = {
                                    Text(text = item)
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        stringResource(id = R.string.category),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W600
                    )
                    LazyRow(modifier = Modifier.fillMaxWidth()) {
                        items(itemsCategory) { item ->
                            FilterChip(
                                modifier = Modifier.padding(end = 6.dp),
                                selected = (item == selectedItemCategory),
                                onClick = { selectedItemCategory = item },
                                label = {
                                    Text(text = item)
                                }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        stringResource(id = R.string.price),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W600
                    )
                    Row(modifier = Modifier.padding(vertical = 8.dp)) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 10.dp)
                        ) {
                            TextFieldPrice(label = R.string.lowest, input = lowestPrice)
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            TextFieldPrice(label = R.string.highest, input = highestPrice)
                        }
                    }

                    Button(
                        onClick = {
                            val brand = selectedItemCategory.ifEmpty { null }
                            val sort = selectedItemList.ifEmpty { null }
                            val low =
                                if (lowestPrice.value.isEmpty()) null else lowestPrice.value.toInt()
                            val high =
                                if (highestPrice.value.isEmpty()) null else highestPrice.value.toInt()
                            //storeViewModel.setQuery(brand, low, high, sort)
                            //storeViewModel.filterAnalytics(dataFilter)
                            setQuery(brand, low, high, sort)
                            filterAnalytics(dataFilter)
                            scope.launch {
                                sheetState.hide()
                            }.invokeOnCompletion {
                                if (!sheetState.isVisible) {
                                    showBottomSheet = false
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Purple,
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = stringResource(id = R.string.tampilkan),
                            fontWeight = FontWeight.W500
                        )
                    }
                }
            }
        }
    }

    Column {
        StoreProductList(
            isClickedGrid,
            products = products,
            onDetailClick,
            onItemAnalytics
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun StoreProductList(
    isClickedGrid: Boolean,
    products: LazyPagingItems<Product>,
    onDetailClick: (id: String) -> Unit,
    onItemAnalytics: (id: String) -> Unit
) {
    var refreshing by remember { mutableStateOf(false) }
    val state = rememberPullRefreshState(refreshing, { products.refresh() })

    if (isClickedGrid) {
        Box(Modifier.pullRefresh(state)) {
            LazyVerticalGrid(GridCells.Fixed(2)) {
                items(
                    count = products.itemCount,
                    key = products.itemKey { it.productId }
                ) { index ->
                    val item = products[index]
                    CardGrid(product = item) {
                        onDetailClick(item!!.productId)
                    }
                }

                when (products.loadState.append) {
                    is LoadState.Loading -> {
                        item(span = { GridItemSpan(maxLineSpan) }) {
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
            PullRefreshIndicator(
                refreshing = refreshing,
                state = state,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    } else {
        Box(Modifier.pullRefresh(state)) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                if (!refreshing) {
                    items(
                        count = products.itemCount,
                        key = products.itemKey { it.productId }
                    ) { index ->
                        val item = products[index]
                        CardList(
                            product = item,
                            onClickCard = {
                                onDetailClick(item!!.productId)
                            },
                            onClickAnalytics = { productId ->
                                onItemAnalytics(productId)
                            }
                        )
                    }
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
            PullRefreshIndicator(
                refreshing = refreshing,
                state = state,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}

@Composable
fun ErrorPage(
    title: String,
    message: String,
    button: Int,
    onButtonClick: () -> Unit,
    alpha: Float
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier.size(128.dp),
            painter = painterResource(id = R.drawable.smartphone),
            contentDescription = ""
        )
        Text(
            modifier = Modifier.padding(top = 5.dp),
            text = title,
            fontSize = 32.sp,
            fontWeight = FontWeight.W500
        )
        Text(text = message, fontSize = 16.sp, fontWeight = FontWeight.W400)
        Button(
            modifier = Modifier
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
fun CardList(
    product: Product?,
    onClickCard: () -> Unit,
    onClickAnalytics: (id: String) -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 5.dp)) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onClickCard()
                    onClickAnalytics(product?.productId ?: "")
                },
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(3.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Box {
                Row(
                    modifier = Modifier.padding(10.dp)
                ) {
                    Card(
                        modifier = Modifier.size(80.dp),
                        shape = RoundedCornerShape(5.dp)
                    ) {
                        Box(modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            if (product!!.image.isEmpty()) {
                                Image(
                                    modifier = Modifier.fillMaxSize(),
                                    painter = painterResource(id = R.drawable.thumbnail),
                                    contentDescription = "image",
                                    contentScale = ContentScale.FillBounds
                                )
                            } else {
                                AsyncImage(
                                    model = product.image,
                                    contentDescription = "Products image"
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
                            text = product!!.productName,
                            fontWeight = FontWeight.W400,
                            fontSize = 12.sp,
                            lineHeight = 15.sp
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(
                            text = currency(product.productPrice),
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
                                product.store,
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
}

@Composable
fun AnimatedFilter() {
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
        ),
        label = ""
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset.Zero,
        end = Offset(x = translateAnim.value, y = translateAnim.value)
    )

    Filter(brush = brush)
}

@Composable
fun Filter(brush: Brush) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.Start
        ) {
            Spacer(
                modifier = Modifier
                    .width(84.dp)
                    .height(32.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(brush)
            )
        }

        Row(horizontalArrangement = Arrangement.End) {
            Spacer(
                modifier = Modifier
                    .height(24.dp)
                    .width(1.dp)
                    .background(brush)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Spacer(
                modifier = Modifier
                    .size(24.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(brush)
            )
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
        ),
        label = ""
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset.Zero,
        end = Offset(x = translateAnim.value, y = translateAnim.value)
    )

    ShimmerCardList(brush = brush)
}

@Composable
fun ShimmerCardList(brush: Brush) {
    Column(Modifier.padding(vertical = 5.dp)) {
        Card(modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(2.dp, CardBorder),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
            )
        ) {
            Box {
                Row(
                    modifier = Modifier.padding(10.dp)
                ) {
                    // image
                    Spacer(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(brush)
                    )
                    Spacer(modifier = Modifier.width(8.dp))

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp)
                    ) {
                        Spacer(
                            modifier = Modifier
                                .height(16.dp)
                                .fillMaxWidth(1f)
                                .background(brush)
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        Spacer(
                            modifier = Modifier
                                .height(16.dp)
                                .fillMaxWidth(1f)
                                .background(brush)
                        )

                        Spacer(modifier = Modifier.height(12.dp))
                        Spacer(
                            modifier = Modifier
                                .height(12.dp)
                                .width(67.dp)
                                .background(brush)
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        Spacer(
                            modifier = Modifier
                                .height(12.dp)
                                .width(67.dp)
                                .background(brush)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CardGrid(product: Product?, onClickCard: () -> Unit) {
    Column(Modifier.padding(top = 5.dp, bottom = 5.dp, end = 5.dp)) {
        Card(
            modifier = Modifier
                .width(186.dp)
                .clickable {
                    onClickCard()
                },
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(3.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column {
                Card(
                    modifier = Modifier.size(186.dp),
                    shape = RoundedCornerShape(
                        topEnd = 8.dp,
                        topStart = 8.dp,
                        bottomEnd = 0.dp,
                        bottomStart = 0.dp
                    )
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        if (product!!.image.isEmpty()) {
                            Image(
                                modifier = Modifier.fillMaxSize(),
                                painter = painterResource(id = R.drawable.thumbnail),
                                contentDescription = "image",
                                contentScale = ContentScale.FillBounds
                            )
                        } else {
                            AsyncImage(
                                modifier = Modifier.fillMaxSize(),
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
                        text = currency(product.productPrice),
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
                            product.store,
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
fun AnimatedGridShimmer() {
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
        ),
        label = ""
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset.Zero,
        end = Offset(x = translateAnim.value, y = translateAnim.value)
    )

    ShimmerGridList(brush = brush)
}

@Composable
fun ShimmerGridList(brush: Brush) {
    Column(Modifier.padding(top = 5.dp, bottom = 5.dp, end = 5.dp)) {
        Card(modifier = Modifier
            .width(186.dp)
            .clickable {},
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(2.dp, CardBorder),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
            )
        ) {
            Column {
                Spacer(
                    modifier = Modifier
                        .size(186.dp)
                        .clip(
                            RoundedCornerShape(
                                topEnd = 8.dp,
                                topStart = 8.dp,
                                bottomEnd = 0.dp,
                                bottomStart = 0.dp
                            )
                        )
                        .fillMaxWidth(fraction = 1f)
                        .background(brush)
                )

                Column(modifier = Modifier.padding(10.dp)) {
                    Spacer(
                        modifier = Modifier
                            .height(16.dp)
                            .fillMaxWidth(fraction = 1f)
                            .background(brush)
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Spacer(
                        modifier = Modifier
                            .height(16.dp)
                            .fillMaxWidth(fraction = 1f)
                            .background(brush)
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    Spacer(
                        modifier = Modifier
                            .height(16.dp)
                            .width(85.dp)
                            .background(brush)
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Spacer(
                        modifier = Modifier
                            .height(16.dp)
                            .width(85.dp)
                            .background(brush)
                    )
                }
            }
        }
    }
}

@Composable
fun TextFieldPrice(
    label: Int,
    input: MutableState<String>,
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
fun SearchDialog(
    openDialog: Boolean,
    onCloseDialog: () -> Unit,
    searchData: String
) {
    if (openDialog) {
        Dialog(
            onDismissRequest = { onCloseDialog() },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            SearchScreen(onCloseDialog, searchData)
        }
    }
}


//@Preview("Light Mode", device = Devices.PIXEL_3)
//@Preview("Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CardListPreview() {
    val product = Product("1","Lenovo",1000000,"","Lenovo","LenovoStore",2,4.0f)
    EcommerceTheme {
        CardList(product = product, onClickCard = {  }, onClickAnalytics = {})
    }
}

//@Preview("Light Mode", device = Devices.PIXEL_3)
//@Preview("Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CardGridPreview() {
    val product = Product("1","Lenovo",1000000,"","Lenovo","LenovoStore",2,4.0f)
    EcommerceTheme {
        CardGrid(product = product, onClickCard = {  })
    }
}

//@Preview("Light Mode", device = Devices.PIXEL_3)
//@Preview("Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ShimmerListPreview(){
    EcommerceTheme {
        Column {
            AnimatedListShimmer()
            AnimatedGridShimmer()
        }
    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class,
    ExperimentalLayoutApi::class
)
@Preview("Light Mode", device = Devices.PIXEL_3)
@Preview("Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun StoreContentPreview(){
    //Mocked Pager flow
    val pager = Pager(PagingConfig(pageSize = 8)) {
        object : PagingSource<Int, Product>() {
            override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
                return LoadResult.Page(
                    data = mockProducts,
                    prevKey = null,
                    nextKey = null
                )
            }

            override fun getRefreshKey(state: PagingState<Int, Product>): Int? = null
        }
    }

    // Collect the paging data as LazyPagingItems
    val productItems = pager.flow.collectAsLazyPagingItems()
    val productFilter = ProductFilter("","Lenovo")

    EcommerceTheme {
        StoreContent(
            searchData = " ",
            products = productItems,
            resetQuery = { /*TODO*/ },
            onButtonRefreshAnalytics = { /*TODO*/ },
            refreshToken = { /*TODO*/ },
            isClickedGrid = false,
            filter = productFilter,
            onDetailClick = {},
            onItemAnalytics = {},
            onButtonResetAnalytics = { /*TODO*/ },
            setSearchText = { /*TODO*/ },
            setQuery = { _, _, _, _ ->  },
            filterAnalytics = {},
            viewListAnalytics = {}
        )
    }
}