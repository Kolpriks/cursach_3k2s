package com.example.koursework.ui.screens.user

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.koursework.ui.components.CarList
import com.example.koursework.ui.components.CarViewModel
import com.example.koursework.ui.components.SearchBarWithHistory
import com.example.koursework.ui.outbox.AppState
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(viewModel: CarViewModel = viewModel()) {
    val context = LocalContext.current
    val userId = AppState.getUserId(context) ?: return

    // Запросим избранное один раз при входе
    LaunchedEffect(userId) {
        viewModel.fetchFavorites(userId)
    }

    var searchQuery by rememberSaveable { mutableStateOf("") }
    val cars = viewModel.cars
    // Набор айдишников избранного из обновлённого списка
    val favorites = viewModel.favorites.map { it.car.id.toString() }
    val coroutine = rememberCoroutineScope()

    Scaffold(
        topBar = { /* TopAppBar как раньше */ },
    ) { paddingValues ->
        val filtered = cars.filter { it.name.contains(searchQuery, ignoreCase = true) }

        Box(Modifier.padding(paddingValues).fillMaxSize()) {
            CarList(
                cars = filtered,
                addFavoriteButtonText = "В избранное",
                onAddFavorite = { viewModel.addFavorite(userId, it.id.toLong()) },
                isAddFavoriteEnabled = { it.id !in favorites }
            )
        }
    }
}
