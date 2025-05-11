package com.example.koursework.ui.screens.manager

import android.widget.Toast
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
import com.example.koursework.ui.components.AssignBuyerBottomSheet
import com.example.koursework.ui.components.CarList
import com.example.koursework.ui.components.CarViewModel
import com.example.koursework.ui.components.SearchBarWithHistory
import com.example.koursework.ui.outbox.AppState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(viewModel: CarViewModel = CarViewModel()) {
    val cars = viewModel.cars

    var searchQuery by rememberSaveable { mutableStateOf("") }
    var isSheetOpen by remember { mutableStateOf(false) }
    var selectedCar by remember { mutableStateOf<com.example.koursework.ui.components.Car?>(null) }
    var email by rememberSaveable { mutableStateOf("") }

    val context = LocalContext.current
    val managerEmail = AppState.getEmail(context) ?: ""

    val scope = rememberCoroutineScope()


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Min),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        SearchBarWithHistory(
                            query = searchQuery,
                            onQueryChange = { searchQuery = it },
                            onSearch = { searchQuery = it },
                            modifier = Modifier.weight(1f)
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { /* можно добавить логику поиска */ },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                            contentColor = MaterialTheme.colorScheme.inverseSurface
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Иконка поиска"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors()
            )
        },
        modifier = Modifier.fillMaxWidth()
    ) { paddingValues ->
        val filtered = cars.filter { it.name.contains(searchQuery, ignoreCase = true) }

        Box(
            modifier = Modifier
                .padding(paddingValues)
                .padding(4.dp)
        ) {
            CarList(
                cars = cars,
                deleteButtonText = "Удалить",
                assignBuyerButtonText = "Назначить покупателю",
                onDeleteCar = { viewModel.deleteCar(it) },
                onAssignBuyer = {
                    selectedCar = it
                    isSheetOpen = true
                }
            )
        }

        if (isSheetOpen && selectedCar != null) {
            AssignBuyerBottomSheet(
                onCloseSheet = {
                    isSheetOpen = false
                    selectedCar = null
                    email = ""
                }
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email покупателя") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            scope.launch {
                                val (ok, msg) = viewModel.assignCar(
                                    selectedCar!!.id.toLong(),
                                    email,
                                    managerEmail
                                )
                                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                                if (ok) {
                                    isSheetOpen = false
                                    selectedCar = null
                                    email = ""
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Назначить")
                    }
                }
            }
        }
    }
}
