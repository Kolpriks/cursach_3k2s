package com.example.koursework.ui.screens.manager

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.koursework.MainActivity
import com.example.koursework.ui.components.CarViewModel
import com.example.koursework.ui.outbox.AppState

@Composable
fun StatisticsScreen(viewModel: CarViewModel = CarViewModel()) {
    val context = LocalContext.current
    var isDarkTheme by remember { mutableStateOf(false) }

    // Email менеджера
    val managerEmail = AppState.getEmail(context) ?: ""
    val count = viewModel.assignmentsCount.value

    // Загружаем статистику
    LaunchedEffect(managerEmail) {
        if (managerEmail.isNotBlank()) {
            viewModel.fetchAssignmentsCount(managerEmail)
        }
    }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        val (userCardRef, soldCardRef, themeRef, signOutRef) = createRefs()

        // Карточка с инфо о пользователе
        Card(
            modifier = Modifier
                .constrainAs(userCardRef) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Пользователь",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.inverseSurface
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Должность: Менеджер по продажам",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.inverseSurface
                )
                Text(
                    text = "Почта: $managerEmail",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.inverseSurface
                )
                Text(
                    text = "Телефон: +7 (925) 100 50 50",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.inverseSurface
                )
            }
        }

        // Карточка «Продано автомобилей»
        Card(
            modifier = Modifier
                .constrainAs(soldCardRef) {
                    top.linkTo(userCardRef.bottom, margin = 16.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Продано автомобилей",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.inverseSurface
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = count?.toString() ?: "…",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.inverseSurface
                )
            }
        }

        // Переключатель темы
        Row(
            modifier = Modifier.constrainAs(themeRef) {
                top.linkTo(soldCardRef.bottom, margin = 16.dp)
                end.linkTo(parent.end)
            },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Сменить тему",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(end = 8.dp)
            )
            Switch(
                checked = isDarkTheme,
                onCheckedChange = {
                    isDarkTheme = it
                    AppState.switchTheme()
                    (context as? Activity)?.recreate()
                }
            )
        }

        // Кнопка выхода
        Button(
            onClick = {
                AppState.logOut(context)
                context.startActivity(
                    Intent(context, MainActivity::class.java)
                        .apply { flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK }
                )
            },
            modifier = Modifier.constrainAs(signOutRef) {
                top.linkTo(themeRef.bottom, margin = 280.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        ) {
            Text(text = "Выйти из аккаунта")
        }
    }
}
