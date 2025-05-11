package com.example.koursework.ui.components

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.koursework.R
import com.example.koursework.network.CarRequest
import com.example.koursework.network.CarResponse
import com.example.koursework.network.FavoriteResponse
import com.example.koursework.network.RetrofitInstance
import kotlinx.coroutines.launch

// Модель данных автомобиля
data class Car(
    val id: String,
    val name: String,
    val price: String,
    val fuelConsumption: String,
    val seats: Int,
    val co2: String,
    val imageBase64: String? = null
)

// ViewModel для работы с автомобилями
class CarViewModel : ViewModel() {
    private val _cars = mutableStateListOf<Car>()
    val cars: List<Car> = _cars

    private val _favorites = mutableStateListOf<FavoriteResponse>()
    val favorites: List<FavoriteResponse> = _favorites
    private val _assignmentsCount = mutableStateOf<Long?>(null)
    val assignmentsCount: State<Long?> = _assignmentsCount
    init {
        fetchCars()
    }

    fun fetchCars() {
        viewModelScope.launch {
            try {
                val response: List<CarResponse> = RetrofitInstance.api.getCars()
                _cars.clear()
                _cars.addAll(response.map { carResp ->
                    Car(
                        id = carResp.id.toString(),
                        name = carResp.name,
                        price = carResp.price,
                        fuelConsumption = carResp.consumption,
                        seats = carResp.seats,
                        co2 = carResp.co2,
                        imageBase64 = carResp.imageBase64
                    )
                })
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun fetchFavorites(userId: Long) {
        viewModelScope.launch {
            try {
                val list = RetrofitInstance.favoriteApi.getAllFavorites()
                _favorites.clear()
                _favorites.addAll(list.filter { it.user.id == userId })
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun favoriteCarsForUser(userId: Long): List<Car> {
        val favCarIds = _favorites.map { it.car.id }
        return _cars.filter { it.id.toLongOrNull() in favCarIds }
    }

    fun addFavorite(userId: Long, carId: Long) {
        viewModelScope.launch {
            try {
                val resp = RetrofitInstance.favoriteApi.createFavorite(userId, carId)
                if (resp.isSuccessful && resp.body() != null) {
                    _favorites.add(resp.body()!!)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun removeFavorite(favoriteId: Long) {
        viewModelScope.launch {
            try {
                val resp = RetrofitInstance.favoriteApi.deleteFavorite(favoriteId)
                if (resp.isSuccessful) {
                    _favorites.removeAll { it.id == favoriteId }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun deleteCar(car: Car) {
        viewModelScope.launch {
            try {
                val id = car.id.toLongOrNull() ?: return@launch
                val response = RetrofitInstance.api.deleteCar(id)
                if (response.isSuccessful) {
                    _cars.remove(car)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun addCar(newCar: CarRequest) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.createCar(newCar)
                if (response.isSuccessful && response.body() != null) {
                    val carResp = response.body()!!
                    _cars.add(
                        Car(
                            id = carResp.id.toString(),
                            name = carResp.name,
                            price = carResp.price,
                            fuelConsumption = carResp.consumption,
                            seats = carResp.seats,
                            co2 = carResp.co2,
                            imageBase64 = carResp.imageBase64
                        )
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateCar(id: Long, updatedCar: CarRequest) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.updateCar(id, updatedCar)
                if (response.isSuccessful && response.body() != null) {
                    fetchCars()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    suspend fun assignCar(
        carId: Long,
        buyerEmail: String,
        managerEmail: String
    ): Pair<Boolean, String> {
        return try {
            val resp = RetrofitInstance.assignmentApi.assignCarToUser(
                carId, buyerEmail, managerEmail
            )
            when {
                resp.isSuccessful -> true to "Машина успешно назначена"
                resp.code() == 400 -> false to (resp.errorBody()?.string() ?: "Пользователь или машина не найдены")
                resp.code() == 409 -> false to (resp.errorBody()?.string() ?: "Пользователь уже назначен")
                else -> false to "Неизвестная ошибка: ${resp.code()}"
            }
        } catch (e: Exception) {
            false to (e.message ?: "Ошибка соединения")
        }
    }
    fun fetchAssignmentsCount(managerEmail: String) {
        viewModelScope.launch {
            try {
                val resp = RetrofitInstance.assignmentApi.getCountByManager(managerEmail)
                if (resp.isSuccessful) {
                    _assignmentsCount.value = resp.body()?.assignmentsCount
                }
            } catch (_: Exception) { }
        }
    }
}
@Composable
fun CarCard(
    car: Car,
    deleteButtonText: String? = null,
    assignBuyerButtonText: String? = null,
    addFavoriteButtonText: String? = null,
    removeFavoriteButtonText: String? = null,
    onDeleteClick: (() -> Unit)? = null,
    onAssignBuyerClick: (() -> Unit)? = null,
    onAddFavoriteClick: (() -> Unit)? = null,
    onRemoveFavoriteClick: (() -> Unit)? = null,
    isAddFavoriteEnabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {
        val (cardRef) = createRefs()

        Card(
            modifier = Modifier.constrainAs(cardRef) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            ),
            elevation = CardDefaults.cardElevation(),
            shape = MaterialTheme.shapes.small,
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                // Изображение
                val imageBitmap: ImageBitmap? = car.imageBase64?.let {
                    try {
                        val imageBytes = Base64.decode(it, Base64.DEFAULT)
                        BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                            ?.asImageBitmap()
                    } catch (e: Exception) {
                        null
                    }
                }

                if (imageBitmap != null) {
                    Image(
                        bitmap = imageBitmap,
                        contentDescription = "Изображение автомобиля",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.lada_2107),
                        contentDescription = "Изображение автомобиля",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                    )
                }

                Spacer(Modifier.height(8.dp))
                Text(text = car.name, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                Text(text = "Цена: ${car.price} рублей", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Расход топлива: ${car.fuelConsumption}", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Мест: ${car.seats}", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Выбросы CO2: ${car.co2}", style = MaterialTheme.typography.bodyMedium)

                Spacer(Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    deleteButtonText?.let {
                        Button(
                            onClick = { onDeleteClick?.invoke() },
                            shape = MaterialTheme.shapes.small
                        ) {
                            Text(it)
                        }
                    }
                    assignBuyerButtonText?.let {
                        Spacer(Modifier.width(8.dp))
                        Button(
                            onClick = { onAssignBuyerClick?.invoke() },
                            shape = MaterialTheme.shapes.small
                        ) {
                            Text(it)
                        }
                    }
                    addFavoriteButtonText?.let {
                        Spacer(Modifier.width(8.dp))
                        Button(
                            onClick = { onAddFavoriteClick?.invoke() },
                            enabled = isAddFavoriteEnabled,
                            shape = MaterialTheme.shapes.small
                        ) {
                            Text(it)
                        }
                    }
                    removeFavoriteButtonText?.let {
                        Spacer(Modifier.width(8.dp))
                        Button(
                            onClick = { onRemoveFavoriteClick?.invoke() },
                            shape = MaterialTheme.shapes.small
                        ) {
                            Text(it)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CarList(
    cars: List<Car>,
    onDeleteCar: ((Car) -> Unit)? = null,
    onAssignBuyer: ((Car) -> Unit)? = null,
    onAddFavorite: ((Car) -> Unit)? = null,
    onRemoveFavorite: ((Car) -> Unit)? = null,
    deleteButtonText: String? = null,
    assignBuyerButtonText: String? = null,
    addFavoriteButtonText: String? = null,
    removeFavoriteButtonText: String? = null,
    isAddFavoriteEnabled: (Car) -> Boolean = { true }
) {
    LazyColumn {
        items(cars) { car ->
            CarCard(
                car = car,
                deleteButtonText = deleteButtonText,
                assignBuyerButtonText = assignBuyerButtonText,
                addFavoriteButtonText = addFavoriteButtonText,
                removeFavoriteButtonText = removeFavoriteButtonText,
                onDeleteClick = { onDeleteCar?.invoke(car) },
                onAssignBuyerClick = { onAssignBuyer?.invoke(car) },
                onAddFavoriteClick = { onAddFavorite?.invoke(car) },
                onRemoveFavoriteClick = { onRemoveFavorite?.invoke(car) },
                isAddFavoriteEnabled = isAddFavoriteEnabled(car)
            )
        }
    }
}
