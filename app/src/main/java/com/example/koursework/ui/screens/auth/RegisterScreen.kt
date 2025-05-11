package com.example.koursework.ui.screens.auth

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.koursework.ui.auth.AuthResult
import com.example.koursework.ui.auth.AuthViewModel
import com.example.koursework.ui.outbox.AppState
import com.example.koursework.ui.theme.MyAppTheme
import kotlinx.coroutines.flow.collectLatest

@Composable
fun RegisterScreen(navController: NavController, authViewModel: AuthViewModel = viewModel()) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordRep by remember { mutableStateOf("") }
    val result by authViewModel.registerResult.collectAsState()

    // Следим за результатом регистрации
    LaunchedEffect(result) {
        result?.let {
            when (it) {
                is AuthResult.Success -> {
                    Toast.makeText(context, "Регистрация прошла успешно, войдите в аккаунт", Toast.LENGTH_SHORT).show()
                    authViewModel.clearResults()
                    navController.navigate("LoginScreen") {
                        popUpTo("RegisterScreen") { inclusive = true }
                    }
                }
                is AuthResult.Error -> {
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                    authViewModel.clearResults()
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(300.dp)) {
            Text(
                text = "Регистрация Аккаунта",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.inverseSurface,
                modifier = Modifier.padding(bottom = 24.dp)
            )
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Почта") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Пароль") },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                    )
                    OutlinedTextField(
                        value = passwordRep,
                        onValueChange = { passwordRep = it },
                        label = { Text("Повторите пароль") },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                    )
                }
            }

            Spacer(Modifier.height(32.dp))

            Button(
                onClick = { navController.navigate("LoginScreen") },
                modifier = Modifier.width(250.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Уже есть аккаунт?")
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    if (email.isNotBlank() && password.isNotBlank() && password == passwordRep) {
                        authViewModel.register(email, password)
                    } else {
                        Toast.makeText(context, "Проверьте введённые данные", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.width(250.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Зарегистрироваться")
            }
        }
    }
}
