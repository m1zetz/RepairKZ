package com.example.repairkz.ui.features.notifiacton

import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.repairkz.Navigation.Routes
import com.example.repairkz.R
import com.example.repairkz.common.enums.OrderRequestStatus
import com.example.repairkz.common.enums.OrderStatus
import com.example.repairkz.common.enums.OrderType
import com.example.repairkz.common.extensions.toRussianString
import com.example.repairkz.ui.features.components.Details
import com.example.repairkz.ui.theme.activeOrderColor
import java.time.LocalDateTime

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
        when (val state = currentState) {
            is NotificationState.Success -> {

                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(state.notifications) { orderType ->
                        when (orderType) {
                            is HistoryItem.ClientItem -> {
                                val status = orderType.data.orderStatus
                                when (orderType.data.type) {
                                    OrderType.ORDER -> {
                                        when (status) {
                                            OrderStatus.RUNNING -> {
                                                NotifyCard(
                                                    "Ваш заказ на ${orderType.data.masterSpec} одобрен и выполняется, мастер свяжется с вами",
                                                    intent = {
                                                        notificationViewModel.handleIntent(
                                                            NotificationIntent.ShowDetails(orderType)
                                                        )
                                                    },
                                                    orderStatus = status,
                                                    changeOrderStatus = {
                                                        notificationViewModel.handleIntent(
                                                            NotificationIntent.ChangeOrderStatus(
                                                                status = OrderStatus.COMPLETED,
                                                                order = orderType
                                                            )
                                                        )
                                                    }
                                                )
                                            }

                                            OrderStatus.COMPLETED -> {
                                                NotifyCard(
                                                    "Ваш заказ на ${orderType.data.masterSpec} завершён",
                                                    intent = {
                                                        notificationViewModel.handleIntent(
                                                            NotificationIntent.ShowDetails(orderType)
                                                        )
                                                    }
                                                )
                                            }

                                            null -> {

                                            }
                                        }

                                    }

                                    OrderType.ORDER_REQUEST -> {
                                        val status = orderType.data.orderRequestStatus
                                        val masterTypeId = orderType.data.masterSpec.resID
                                        val data =
                                            orderType.data.orderDate.let { LocalDateTime.parse(it) }
                                                .toRussianString()
                                        when (status) {
                                            OrderRequestStatus.ACCEPTED -> {
                                                NotifyCard(
                                                    "Ваш заказ на ${stringResource(masterTypeId)} на ${data} был принят, ожидайте звонка мастера",
                                                    intent = {
                                                        notificationViewModel.handleIntent(
                                                            NotificationIntent.ShowDetails(orderType)
                                                        )
                                                    }
                                                )
                                            }

                                            OrderRequestStatus.REJECTED -> {
                                                NotifyCard(
                                                    "Ваш заказ на ${stringResource(masterTypeId)} на ${data} был отклонен",
                                                    intent = {
                                                        notificationViewModel.handleIntent(
                                                            NotificationIntent.ShowDetails(orderType)
                                                        )
                                                    }
                                                )
                                            }

                                            OrderRequestStatus.PENDING -> {
                                                NotifyCard(
                                                    "Вы заказали ${stringResource(masterTypeId)} на ${data}, ожидайте ответа мастера",
                                                    intent = {
                                                        notificationViewModel.handleIntent(
                                                            NotificationIntent.ShowDetails(orderType)
                                                        )
                                                    }
                                                )
                                            }

                                            null -> {

                                            }
                                        }


                                    }
                                }

                            }

                            is HistoryItem.MasterItem -> {
                                val status = orderType.data.orderStatus
                                when (orderType.data.type) {
                                    OrderType.ORDER -> {
                                        when (status) {
                                            OrderStatus.RUNNING -> {
                                                NotifyCard(
                                                    "У вас активный заказ",
                                                    intent = {
                                                        notificationViewModel.handleIntent(
                                                            NotificationIntent.ShowDetails(orderType)
                                                        )
                                                    },
                                                    orderStatus = status
                                                )
                                            }

                                            OrderStatus.COMPLETED -> {
                                                NotifyCard(
                                                    "Ваш заказ завершен",
                                                    intent = {
                                                        notificationViewModel.handleIntent(
                                                            NotificationIntent.ShowDetails(orderType)
                                                        )
                                                    }
                                                )
                                            }

                                            null -> {

                                            }
                                        }

                                    }

                                    OrderType.ORDER_REQUEST -> {
                                        val status = orderType.data.orderRequestStatus
                                        when (status) {
                                            OrderRequestStatus.ACCEPTED -> {
                                                NotifyCard(
                                                    "Вас заказал клиент с адресом ${orderType.data.clientAddress}",
                                                    intent = {
                                                        notificationViewModel.handleIntent(
                                                            NotificationIntent.ShowDetails(orderType)
                                                        )
                                                    }

                                                )
                                            }

                                            OrderRequestStatus.REJECTED -> {
                                                NotifyCard(
                                                    "Заказ клиента на адрес ${orderType.data.clientAddress} был отклонен",
                                                    intent = {
                                                        notificationViewModel.handleIntent(
                                                            NotificationIntent.ShowDetails(orderType)
                                                        )
                                                    }
                                                )
                                            }

                                            OrderRequestStatus.PENDING -> {
                                                NotifyCard(
                                                    "Вас заказал клиент на адрес ${orderType.data.clientAddress}",
                                                    intent = {
                                                        notificationViewModel.handleIntent(
                                                            NotificationIntent.ShowDetails(orderType)
                                                        )
                                                    },
                                                    status = orderType.data.orderRequestStatus,
                                                    accept = {
                                                        notificationViewModel.handleIntent(
                                                            NotificationIntent.AcceptOrReject(
                                                                OrderRequestStatus.ACCEPTED,
                                                                orderType
                                                            )
                                                        )
                                                    },
                                                    reject = {
                                                        notificationViewModel.handleIntent(
                                                            NotificationIntent.AcceptOrReject(
                                                                OrderRequestStatus.REJECTED,
                                                                orderType
                                                            )
                                                        )
                                                    }
                                                )
                                            }

                                            null -> {

                                            }
                                        }

                                    }
                                }
                            }
                        }

                    }
                }
                state.selectedOrder?.let { order ->
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

            is NotificationState.Error -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(state.message)
                }
            }

            NotificationState.Loading -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                }
            }
        }

    }


}

@Composable
fun NotifyCard(
    text: String,
    intent: () -> Unit,
    accept: (() -> Unit)? = null,
    reject: (() -> Unit)? = null,
    color: Color = MaterialTheme.colorScheme.onSurface,
    status: OrderRequestStatus? = null,
    orderStatus: OrderStatus? = null,
    changeOrderStatus: (() -> Unit)? = null,
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
        ),
        colors = if (orderStatus == OrderStatus.RUNNING)
            CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        else
            CardDefaults.cardColors()

    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
        ) {
            Text(
                text = text,
                color = color
            )
            when (status) {
                OrderRequestStatus.ACCEPTED -> {
                    Text(stringResource(R.string.accepted))
                }

                OrderRequestStatus.REJECTED -> {
                    Text(stringResource(R.string.rejected))
                }

                OrderRequestStatus.PENDING -> {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        IconButton(
                            modifier = Modifier
                                .padding(4.dp)
                                .clip(
                                    RoundedCornerShape(9.dp)
                                )
                                .background(com.example.repairkz.ui.theme.accept),
                            onClick = {
                                accept?.invoke()
                            }
                        ) {
                            Icon(Icons.Default.Check, contentDescription = null)
                        }
                        IconButton(
                            modifier = Modifier
                                .padding(4.dp)
                                .clip(
                                    RoundedCornerShape(9.dp)
                                )

                                .background(com.example.repairkz.ui.theme.reject),
                            onClick = {
                                reject?.invoke()
                            }
                        ) {
                            Icon(Icons.Default.Close, contentDescription = null)
                        }
                    }

                }

                null -> {

                }
            }
            when (orderStatus) {
                OrderStatus.RUNNING -> {
                    if (changeOrderStatus != null){
                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                changeOrderStatus()
                            }
                        ) {
                            Text("Завершить заказ")
                        }
                    }

                }

                else -> {}
            }
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