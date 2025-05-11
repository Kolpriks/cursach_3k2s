package com.example.koursework.ui.screens.manager

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Base64
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import coil3.compose.rememberAsyncImagePainter
import com.example.koursework.network.CarRequest
import com.example.koursework.ui.components.CarViewModel
import com.example.koursework.ui.theme.MyAppTheme
import java.io.InputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditScreen(viewModel: CarViewModel = CarViewModel()) {
    val context = LocalContext.current

    // Поля формы
    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var consumption by remember { mutableStateOf("") }
    var seats by remember { mutableStateOf("") }
    var co2 by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    // Функция для получения имени файла из Uri
    fun getFileNameFromUri(context: Context, uri: Uri): String {
        var fileName = ""
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (nameIndex >= 0) {
                    fileName = it.getString(nameIndex)
                }
            }
        }
        return fileName
    }

    // Функция для конвертации изображения в Base64
    fun uriToBase64(context: Context, uri: Uri): String? {
        return try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            val bytes = inputStream?.readBytes()
            inputStream?.close()
            if (bytes != null) Base64.encodeToString(bytes, Base64.NO_WRAP) else null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    val selectedImageName = selectedImageUri?.let { getFileNameFromUri(context, it) } ?: ""

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Добавить автомобиль") },
                colors = TopAppBarDefaults.topAppBarColors()
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier
            .padding(paddingValues)
            .padding(16.dp)) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Марка и модель") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = price,
                onValueChange = { price = it },
                label = { Text("Цена") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = consumption,
                onValueChange = { consumption = it },
                label = { Text("Расход топлива (л/100км)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = seats,
                onValueChange = { seats = it },
                label = { Text("Количество мест") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = co2,
                onValueChange = { co2 = it },
                label = { Text("Выбросы CO2") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Описание") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = selectedImageName,
                    onValueChange = {},
                    label = { Text("Выбранное изображение") },
                    readOnly = true,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(onClick = { imagePickerLauncher.launch("image/*") }) {
                    Icon(imageVector = Icons.Default.FileUpload, contentDescription = "Выбрать изображение")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    if (name.isBlank() || price.isBlank() || consumption.isBlank() ||
                        seats.isBlank() || co2.isBlank()
                    ) {
                        Toast.makeText(context, "Заполните все обязательные поля", Toast.LENGTH_SHORT).show()
                    } else if (selectedImageUri == null) {
                        Toast.makeText(context, "Выберите изображение", Toast.LENGTH_SHORT).show()
                    } else {
                        val imageBase64 = uriToBase64(context, selectedImageUri!!)
                        if (imageBase64 == null) {
                            Toast.makeText(context, "Ошибка обработки изображения", Toast.LENGTH_SHORT).show()
                        } else {
                            // Формируем запрос для создания автомобиля
                            val newCarRequest = CarRequest(
                                name = name,
                                price = price,
                                consumption = consumption,
                                seats = seats.toIntOrNull() ?: 0,
                                co2 = co2,
                                image = imageBase64,
                                description = description
                            )
                            viewModel.addCar(newCarRequest)
                            Toast.makeText(context, "Машина добавлена", Toast.LENGTH_SHORT).show()
                            // Очистка полей формы
                            name = ""
                            price = ""
                            consumption = ""
                            seats = ""
                            co2 = ""
                            description = ""
                            selectedImageUri = null
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Сохранить")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EditScreenPreview() {
    MyAppTheme {
        EditScreen()
    }
}
