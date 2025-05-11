package com.example.koursework

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.List
import com.example.koursework.ui.components.NavHostAndNavBar
import com.example.koursework.ui.components.NavItem
import com.example.koursework.ui.outbox.AppState
import com.example.koursework.ui.screens.user.AdditionalScreen
import com.example.koursework.ui.screens.user.CarScreen
import com.example.koursework.ui.screens.user.HomeScreen
import com.example.koursework.ui.theme.MyAppTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class UserActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Проверяем сессию
        if (!AppState.isLoggedIn(this) || AppState.getRole(this) != "user") {
            startActivity(Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            })
            return
        }

        setContent {
            MyAppTheme {
                val navController = rememberNavController()
                val navItems = listOf(
                    NavItem("List", Icons.Default.DirectionsCar, "Каталог"),
                    NavItem("Favorite", Icons.Default.FavoriteBorder, "Избранное"),
                    NavItem("Additional", Icons.Default.List, "Cправка")
                )
                NavHostAndNavBar(
                    navController = navController,
                    navHostContent = {
                        NavHost(navController, startDestination = "List") {
                            composable("List") { HomeScreen() }
                            composable("Favorite") { CarScreen() }
                            composable("Additional") { AdditionalScreen() }
                        }
                    },
                    navItems = navItems
                )
            }
        }
    }
}
