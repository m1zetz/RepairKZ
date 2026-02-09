package com.example.repairkz.ui.features.notifiacton

import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.repairkz.common.extensions.toRussianString
import com.example.repairkz.ui.features.components.Details

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(notificationViewModel: NotificationViewModel) {
    val currentState by notificationViewModel.state.collectAsState()
    val sheetState = rememberModalBottomSheetState()

    LaunchedEffect(Unit) {
        notificationViewModel.handleIntent(NotificationIntent.GetNotifications)
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        if (currentState.isLoading) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
            }

        } else if (currentState.error != null) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(currentState.error!!)
            }

        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(currentState.notifications) {order ->
                    NotifyCard(
                        "Вы заказали мастера ${order.masterSpecialization} на ${order.time.toRussianString()}",
                        intent = {
                            notificationViewModel.handleIntent(NotificationIntent.ShowDetails(order))
                        }
                    )
                }
            }

        }
    }

    currentState.selectedOrder?.let { order ->
        ModalBottomSheet(
            onDismissRequest = {
                notificationViewModel.handleIntent(NotificationIntent.HideDetails)
            },
            sheetState = sheetState
        ) {
            Details(order)
        }
    }


}

@Composable
fun NotifyCard(
    text: String,
    intent: () -> Unit,
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
                ) {
                    intent()
                }

            )
        }

    }
}