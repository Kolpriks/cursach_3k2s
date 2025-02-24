package com.example.koursework.ui.screens.auth

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester.Companion.createRefs
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.koursework.UserActivity
import com.example.koursework.ui.theme.MyAppTheme
import androidx.constraintlayout.compose.*
import com.example.koursework.ManagerActivity

@Composable
fun LoginScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current
    val (boxBeforeImageRef, imageRef, boxAfterImageRef) = createRefs()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(300.dp)
        ) {
            Text(
                text = "Войдите в Аккаунт",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 24.dp),
                color = MaterialTheme.colorScheme.inverseSurface
            )
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
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
                }
            }

            Button(
                onClick = { navController.navigate("RegisterScreen") },
                modifier = Modifier
                    .width(250.dp)
                    .padding(top = 160.dp)
                ,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.background
                )
            ) {
                Text(
                    text = "Или зарегистрируйстесь",
                    modifier = Modifier
                        .padding(top = 8.dp, bottom = 8.dp)
                )
            }

            Button(
                onClick = {
                    if (email == "1") {
                        val intent = Intent(context, ManagerActivity::class.java)
                        context.startActivity(intent)
                    } else {
                        val intent = Intent(context, UserActivity::class.java)
                        context.startActivity(intent)
                    }

                },
                modifier = Modifier
                    .width(250.dp)
                    .padding(top = 14.dp)
                ,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.background
                )
            ) {
                Text(
                    text = "Войти",
                    modifier = Modifier
                        .padding(top = 8.dp, bottom = 8.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "Login Screen Preview")
@Composable
fun LoginScreenPreview() {
    MyAppTheme {
        val navController = rememberNavController()
        LoginScreen(navController)
    }
}
