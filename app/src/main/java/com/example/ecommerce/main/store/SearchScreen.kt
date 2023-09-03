package com.example.ecommerce.main.store

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

@Composable
fun SearchBarScreen() {
    var searchText = remember { mutableStateOf("") }
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

    Column(modifier = Modifier.fillMaxWidth()
        .padding(16.dp)
        .background(color = Color.Gray.copy(alpha = 0.1f)).animateContentSize()
    ) {
        CustomSearchBar(
            modifier = Modifier.fillMaxWidth(),
            hint = "Search items...",
            onSearch = {
                searchResults = listOf("Apple", "Banana", "Cherry", "Grapes", "Orange")
            },
            searchText
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(filteredResults) { item ->
                Text(text = item, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
fun CustomSearchBar(
    modifier: Modifier = Modifier,
    hint: String = "Search",
    onSearch: (String) -> Unit,
    searchText:MutableState<String>,
) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
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
        singleLine = true,
        maxLines = 1,
        value = searchText.value,
        onValueChange = {
            searchText.value = it
            onSearch(it)
        },
        leadingIcon = {
            Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
        },
        trailingIcon = {
            Icon(
                modifier = Modifier.clickable {
                },
                imageVector = Icons.Default.Close,
                contentDescription = "close")
        }
    )
}