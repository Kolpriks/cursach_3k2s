package com.example.koursework.ui.screens.manager

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.koursework.MainActivity
import com.example.koursework.R
import com.example.koursework.ui.outbox.AppState
import com.example.koursework.ui.theme.MyAppTheme

@Composable
fun StatisticsScreen() {
    val context = LocalContext.current
    var isDarkTheme by remember { mutableStateOf(false) }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        val (
            userInfoCardRef,
            closedDealsCardRef,
            soldCarsCardRef,
            signOutButtonRef,
            changeThemeButtonRef
        ) = createRefs()

        Card(
            modifier = Modifier.constrainAs(userInfoCardRef) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            shape = MaterialTheme.shapes.medium,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                val (userTitleRef, positionRef, emailRef, phoneRef) = createRefs()
                Text(
                    text = "Пользователь",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.inverseSurface,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.constrainAs(userTitleRef) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    }
                )
                Text(
                    text = "Должность: Менеджер по продажам",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.inverseSurface,
                    modifier = Modifier.constrainAs(positionRef) {
                        top.linkTo(userTitleRef.bottom, margin = 8.dp)
                        start.linkTo(parent.start)
                    }
                )
                Text(
                    text = "Почта: example@mail.com",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.inverseSurface,
                    modifier = Modifier.constrainAs(emailRef) {
                        top.linkTo(positionRef.bottom, margin = 4.dp)
                        start.linkTo(parent.start)
                    }
                )
                Text(
                    text = "Телефон: +7 (925) 100 50 50",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.inverseSurface,
                    modifier = Modifier.constrainAs(phoneRef) {
                        top.linkTo(emailRef.bottom, margin = 4.dp)
                        start.linkTo(parent.start)
                    }
                )
            }
        }

        Card(
            modifier = Modifier.constrainAs(closedDealsCardRef) {
                top.linkTo(userInfoCardRef.bottom, margin = 16.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            shape = MaterialTheme.shapes.medium,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                val (titleRef, dealsCountRef) = createRefs()
                Text(
                    text = "Закрыто за месяц",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.inverseSurface,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.constrainAs(titleRef) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    }
                )
                Text(
                    text = "Сделок: 4",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.inverseSurface,
                    modifier = Modifier.constrainAs(dealsCountRef) {
                        top.linkTo(titleRef.bottom, margin = 4.dp)
                        start.linkTo(parent.start)
                    }
                )
            }
        }

        Card(
            modifier = Modifier.constrainAs(soldCarsCardRef) {
                top.linkTo(closedDealsCardRef.bottom, margin = 16.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            shape = MaterialTheme.shapes.medium,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                val (titleRef, soldCountRef) = createRefs()
                Text(
                    text = "Продано автомобилей",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.inverseSurface,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.constrainAs(titleRef) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    }
                )
                Text(
                    text = "30",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.inverseSurface,
                    modifier = Modifier.constrainAs(soldCountRef) {
                        top.linkTo(titleRef.bottom, margin = 4.dp)
                        start.linkTo(parent.start)
                    }
                )
            }
        }

        Row(
            modifier = Modifier.constrainAs(changeThemeButtonRef) {
                top.linkTo(soldCarsCardRef.bottom, margin = 16.dp)
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
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.colorScheme.primary,
                    uncheckedThumbColor = MaterialTheme.colorScheme.secondary,
                    checkedTrackColor = MaterialTheme.colorScheme.primaryContainer,
                    uncheckedTrackColor = MaterialTheme.colorScheme.secondaryContainer
                )
            )
        }
        Button(
            onClick = {
                AppState.logOut()
                val intent = Intent(context, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                context.startActivity(intent)
            },
            shape = MaterialTheme.shapes.small,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            modifier = Modifier.constrainAs(signOutButtonRef) {
                top.linkTo(changeThemeButtonRef.bottom, margin = 280.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        ) {
            Text(text = "Выйти из Аккаунта")
        }
    }
}
