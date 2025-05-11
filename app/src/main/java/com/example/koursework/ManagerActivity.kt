package com.example.koursework

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.koursework.ui.components.NavHostAndNavBar
import com.example.koursework.ui.components.NavItem
import com.example.koursework.ui.outbox.AppState
import com.example.koursework.ui.screens.manager.EditScreen
import com.example.koursework.ui.screens.manager.ListScreen
import com.example.koursework.ui.screens.manager.StatisticsScreen
import com.example.koursework.ui.theme.MyAppTheme

class ManagerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Если нет логина или роль другая — на экран входа
        if (!AppState.isLoggedIn(this) || AppState.getRole(this) != "manager") {
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
                    NavItem("Edit", Icons.Default.Edit, "Редактирование"),
                    NavItem("Statistics", Icons.Default.MoreHoriz, "Статистика")
                )
                NavHostAndNavBar(
                    navController = navController,
                    navHostContent = {
                        NavHost(navController, startDestination = "List") {
                            composable("List") { ListScreen() }
                            composable("Edit") { EditScreen() }
                            composable("Statistics") { StatisticsScreen() }
                        }
                    },
                    navItems = navItems
                )
            }
        }
    }
}
