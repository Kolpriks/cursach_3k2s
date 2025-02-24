package com.example.koursework.ui.screens.user

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.koursework.MainActivity
import com.example.koursework.ui.theme.MyAppTheme
import com.example.koursework.R
import com.example.koursework.ui.outbox.AppState


@Composable
fun AdditionalScreen() {
    val context = LocalContext.current

    // Состояние для переключателя темы
    var isDarkTheme by remember { mutableStateOf(AppState.isDarkTheme) }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Создаем ссылки для всех элементов
        val (addressCardRef, workTimeCardRef, contactCardRef, imageRef, signOutB, themeSwitchRef) = createRefs()

        // Карточка с адресом
        Card(
            modifier = Modifier
                .constrainAs(addressCardRef) {
                    top.linkTo(parent.top, margin = 16.dp)
                    start.linkTo(parent.start, margin = 16.dp)
                    end.linkTo(parent.end, margin = 16.dp)
                },
            shape = MaterialTheme.shapes.medium,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            )
        ) {
            Column(
                modifier = Modifier
                    .width(350.dp)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Адрес:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.inverseSurface,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "г. Москва ул. Пупкина д. 123 корпус 1",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.inverseSurface,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        // Карточка с временем работы
        Card(
            modifier = Modifier
                .constrainAs(workTimeCardRef) {
                    top.linkTo(addressCardRef.bottom, margin = 16.dp)
                    start.linkTo(parent.start, margin = 16.dp)
                    end.linkTo(parent.end, margin = 16.dp)
                },
            shape = MaterialTheme.shapes.medium,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            )
        ) {
            Column(
                modifier = Modifier
                    .width(350.dp)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Время работы:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.inverseSurface,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Будние дни: 10:00 - 22:00\nСуббота: 10:00 - 20:00\nВоскресенье - выходной",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.inverseSurface,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        // Карточка с контактными данными
        Card(
            modifier = Modifier
                .constrainAs(contactCardRef) {
                    top.linkTo(workTimeCardRef.bottom, margin = 16.dp)
                    start.linkTo(parent.start, margin = 16.dp)
                    end.linkTo(parent.end, margin = 16.dp)
                },
            shape = MaterialTheme.shapes.medium,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            )
        ) {
            Column(
                modifier = Modifier
                    .width(350.dp)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Контактные данные",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.inverseSurface,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Номер менеджера: +7 (900) 000 00 00",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.inverseSurface,
                    modifier = Modifier.padding(top = 8.dp)
                )
                Text(
                    text = "Почта салона: saloooon@gmail.com",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.inverseSurface,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        // Изображение
        Box(
            modifier = Modifier
                .constrainAs(imageRef) {
                    top.linkTo(contactCardRef.bottom, margin = 16.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .size(200.dp)
                .background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.shema_proezda),
                contentDescription = "Описание картинки",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        Row(
            modifier = Modifier
                .constrainAs(themeSwitchRef) {
                    top.linkTo(imageRef.bottom, margin = 32.dp)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Сменить тему приложения",
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
            modifier = Modifier
                .width(200.dp)
                .constrainAs(signOutB) {
                    top.linkTo(themeSwitchRef.bottom, margin = 60.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        ) {
            Text(text = "Выйти из Аккаунта")
        }
    }
}

@Preview(showBackground = true, name = "Login Screen Preview")
@Composable
fun AdditionalPreview() {
    MyAppTheme {
        AdditionalScreen()
    }
}