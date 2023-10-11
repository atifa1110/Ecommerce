package com.example.ecommerce.main.store

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.core.api.response.BaseResponse

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchScreen(
    onCloseDialog: () -> Unit = {},
    searchData: String
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val storeViewModel: StoreViewModel = hiltViewModel()
    var isLoading by rememberSaveable { mutableStateOf(false) }

    if (isLoading) {
        Column(
            Modifier
                .fillMaxWidth()
                .height(250.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator()
        }
    }

    val searchText = remember { mutableStateOf(searchData) }
    var searchResults by remember { mutableStateOf(emptyList<String>()) }
    val filteredResults by remember(searchResults, searchText) {
        derivedStateOf {
            if (searchText.value.isEmpty()) {
                searchResults
            } else {
                searchResults.filter { it.contains(searchText.value, ignoreCase = true) }
            }
        }
    }

    storeViewModel.searchResult.observe(lifecycleOwner) {
        when (it) {
            is BaseResponse.Loading -> {
                isLoading = true
            }

            is BaseResponse.Success -> {
                isLoading = false
                searchResults = it.data?.data ?: emptyList()
                storeViewModel.searchAnalytics(searchText.value)
            }

            else -> {}
        }
    }

    val visible by remember { mutableStateOf(true) }
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(animationSpec = tween(durationMillis = 1000)),
        exit = slideOutVertically(animationSpec = tween(durationMillis = 1000))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(start = 2.dp)
        ) {
            CustomSearchBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester)
                    .onFocusChanged {
                        if (it.isFocused) {
                            keyboardController?.show()
                        }
                    },
                storeViewModel,
                hint = "Search",
                onSearch = {
                    storeViewModel.searchProductList(it)
                },
                searchText,
                onCloseDialog
            )

            filteredResults.forEach { item ->
                ListItem(
                    modifier = Modifier.clickable {
                        searchText.value = item
                        storeViewModel.setSearchText(item)
                        onCloseDialog()
                    },
                    headlineContent = {
                        Text(
                            text = item,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.W400
                        )
                    },
                    leadingContent = {
                        Icon(
                            modifier = Modifier.size(18.dp),
                            imageVector = Icons.Default.Search,
                            contentDescription = null
                        )
                    },
                    trailingContent = {
                        Icon(
                            modifier = Modifier.size(18.dp),
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = null
                        )
                    },
                    colors = ListItemDefaults.colors(Color.Transparent)
                )
            }
        }
    }
}

@Composable
fun CustomSearchBar(
    modifier: Modifier = Modifier,
    storeViewModel: StoreViewModel,
    hint: String = "Search",
    onSearch: (String) -> Unit,
    searchText: MutableState<String>,
    onCloseDialog: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 16.dp,
                start = 16.dp,
                end = 16.dp
            )
    ) {
        OutlinedTextField(
            modifier = modifier,
            placeholder = {
                Text(
                    text = hint,
                    style = MaterialTheme.typography.bodyMedium,
                )
            },
            textStyle = MaterialTheme.typography.bodyMedium,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    storeViewModel.setSearchText(searchText.value)
                    onCloseDialog()
                }
            ),
            singleLine = true,
            maxLines = 1,
            value = searchText.value,
            onValueChange = {
                searchText.value = it
                onSearch(it)
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search"
                )
            },
            trailingIcon = {
                Icon(
                    modifier = Modifier.clickable {
                        if (searchText.value.isNotEmpty()) {
                            searchText.value = ""
                            storeViewModel.setSearchText("")
                        } else {
                            onCloseDialog()
                        }
                    },
                    imageVector = Icons.Outlined.Cancel,
                    contentDescription = "close"
                )
            }
        )
    }
}
