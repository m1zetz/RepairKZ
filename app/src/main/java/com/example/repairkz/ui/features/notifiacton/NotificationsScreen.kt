package com.example.repairkz.ui.features.notifiacton

import android.app.Activity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp

@Composable
fun NotificationsScreen() {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(1){
            NotifyCard("Вы оформили заказ сантехника на 15:00, 29 окт.")
        }
    }
}

@Composable
fun NotifyCard(
    text: String,
    intent: () -> Unit = {},
    color: Color = MaterialTheme.colorScheme.onSurface
) {
    val interactionSource = remember { MutableInteractionSource() }
    Card(
        modifier = Modifier
            .widthIn(max = 220.dp)
            .padding(horizontal = 8.dp, vertical = 8.dp),
        shape = RoundedCornerShape(
            topStart = 25.dp,
            topEnd = 25.dp,
            bottomStart = 5.dp,
            bottomEnd = 25.dp
        )
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
        ) {
            Text(
                text = text,
                color = color
            )
            Text(
                "Подробности",
                textDecoration = TextDecoration.Underline,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable(
                    interactionSource = interactionSource,
                    indication = null
                ){
                    intent()
                }

            )
        }

    }
}