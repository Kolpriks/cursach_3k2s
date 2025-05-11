package com.example.koursework.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.koursework.utils.SearchHistoryManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarWithHistory(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    var expanded by remember { mutableStateOf(false) }
    var history by remember { mutableStateOf(emptyList<String>()) }

    LaunchedEffect(expanded) {
        if (expanded) history = SearchHistoryManager.getHistory(context)
    }

    Column(modifier) {
        OutlinedTextField(
            value = query,
            onValueChange = { onQueryChange(it) },
            modifier = modifier
                .fillMaxWidth()
                .onFocusChanged { focus -> expanded = focus.isFocused },
            placeholder = { Text("Поиск по названию авто") },
            singleLine = true,
            trailingIcon = {
                if (query.isNotEmpty()) {
                    IconButton(
                        onClick = {
                            onQueryChange("")
                            keyboardController?.hide()
                        }
                    ) {
                        Icon(Icons.Filled.Clear, contentDescription = "Очистить")
                    }
                }
            },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = {
                onSearch(query)
                SearchHistoryManager.addQuery(context, query)
                expanded = false
                keyboardController?.hide()
            })
        )

        DropdownMenu(
            expanded = expanded && history.isNotEmpty(),
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            history.forEach { item ->
                DropdownMenuItem(
                    onClick = {
                        onQueryChange(item)
                        onSearch(item)
                        SearchHistoryManager.addQuery(context, item)
                        expanded = false
                        keyboardController?.hide()
                    },
                    text = { Text(item) }
                )
            }
            Divider(thickness = 1.dp)
            DropdownMenuItem(
                onClick = {
                    SearchHistoryManager.clearHistory(context)
                    history = emptyList()
                },
                text = { Text("Очистить историю") }
            )
        }
    }
}