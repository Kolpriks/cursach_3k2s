package com.example.koursework.ui.screens.user

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.koursework.ui.components.Car
import com.example.koursework.ui.components.CarCard
import com.example.koursework.ui.components.CarList
import com.example.koursework.ui.components.CarViewModel
import com.example.koursework.ui.outbox.AppState
import kotlinx.coroutines.launch

@Composable
fun CarScreen(viewModel: CarViewModel = viewModel()) {
    val context = LocalContext.current
    val userId = AppState.getUserId(context) ?: return

    // Обновляем избранное при составлении
    SideEffect {
        viewModel.fetchFavorites(userId)
    }

    // Список избранных машин для текущего пользователя
    val favCars by remember {
        derivedStateOf { viewModel.favoriteCarsForUser(userId) }
    }

    // Один скроллируемый список внутри CarList
    Box(
        modifier = Modifier
            .padding(4.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {
        CarList(
            cars = favCars,
            removeFavoriteButtonText = "Убрать из избранного",
            onRemoveFavorite = { car ->
                // находим объект FavoriteResponse по car.id внутри viewModel
                val fav = viewModel.favorites.firstOrNull { it.car.id.toString() == car.id }
                fav?.let { viewModel.removeFavorite(it.id) }
            }
        )
    }
}
