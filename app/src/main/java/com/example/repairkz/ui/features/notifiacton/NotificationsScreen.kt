package com.example.repairkz.ui.features.notifiacton

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.repairkz.R
import com.example.repairkz.common.enums.MasterSpetializationsEnum
import com.example.repairkz.common.enums.OrderRequestStatus
import com.example.repairkz.common.enums.OrderStatus
import com.example.repairkz.common.enums.OrderType
import com.example.repairkz.ui.features.components.Details
import com.example.repairkz.ui.features.notifiacton.NotificationIntent.*

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(notificationViewModel: NotificationViewModel) {
    val currentState by notificationViewModel.state.collectAsState()
    val sheetState = rememberModalBottomSheetState()

    LaunchedEffect(Unit) {
        notificationViewModel.handleIntent(GetNotifications)
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        when (val state = currentState) {
            is NotificationState.Success -> {

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    items(state.notifications) { orderType ->
                        when (orderType) {

                            is HistoryItem.ClientItem -> {

                                val status = orderType.data.orderStatus
                                val requestStatus = orderType.data.orderRequestStatus
                                val type = orderType.data.masterSpec.resID

                                when (orderType.data.type) {
                                    OrderType.ORDER -> {
                                        val text = when (status) {
                                            OrderStatus.RUNNING -> stringResource(R.string.order_running, stringResource(type))
                                            OrderStatus.COMPLETED -> stringResource(R.string.order_complete, stringResource(type))
                                            OrderStatus.REJECTED -> stringResource(R.string.order_reject, stringResource(type))
                                            null -> stringResource(R.string.not_defined)
                                        }

                                        NotifyCard(
                                            text,
                                            intent = {
                                                notificationViewModel.handleIntent(
                                                    ShowDetails(orderType)
                                                )

                                            },
                                            changeOrderStatus = if (status == OrderStatus.RUNNING) {
                                                {

                                                    notificationViewModel.handleIntent(
                                                        ChangeOrderStatus(
                                                            status = OrderStatus.COMPLETED,
                                                            order = orderType
                                                        )
                                                    )
                                                }
                                            } else {
                                                null
                                            },
                                            time = orderType.createdAt,
                                            orderStatus = status,
                                            requestStatus = requestStatus
                                        )

                                    }

                                    OrderType.ORDER_REQUEST -> {
                                        val spec = orderType.data.masterSpec
                                        val text = if (spec == MasterSpetializationsEnum.UNKNOWN)
                                            stringResource(R.string.order_request_unknown)
                                        else
                                            stringResource(
                                                R.string.order_request,
                                                stringResource(spec.resID)
                                            )

                                        NotifyCard(
                                            text,
                                            intent = {
                                                notificationViewModel.handleIntent(
                                                    NotificationIntent.ShowDetails(orderType)
                                                )
                                            },
                                            time = orderType.createdAt,
                                            orderStatus = null,
                                            requestStatus = null
                                        )


                                    }
                                }

                            }

                            is HistoryItem.MasterItem -> {
                                val status = orderType.data.orderStatus
                                val requestStatus = orderType.data.orderRequestStatus
                                when (orderType.data.type) {
                                    OrderType.ORDER -> {
                                        val address = orderType.data.clientAddress
                                        val text = when (status) {
                                            OrderStatus.RUNNING -> stringResource(R.string.active_order_notify, address)
                                            OrderStatus.COMPLETED -> stringResource(R.string.master_order_complete, address)
                                            OrderStatus.REJECTED -> stringResource(R.string.master_order_reject, address)
                                            null -> stringResource(R.string.not_defined)
                                        }
                                        NotifyCard(
                                            text,
                                            intent = {
                                                notificationViewModel.handleIntent(
                                                    ShowDetails(orderType)
                                                )
                                            },
                                            orderStatus = status,
                                            time = orderType.createdAt,
                                            requestStatus = requestStatus,
                                        )
                                    }

                                    OrderType.ORDER_REQUEST -> {
                                        NotifyCard(
                                            stringResource(
                                                R.string.master_order_request,
                                                orderType.data.clientAddress
                                            ),
                                            intent = {
                                                notificationViewModel.handleIntent(
                                                    NotificationIntent.ShowDetails(orderType)
                                                )
                                            },
                                            requestStatus = orderType.data.orderRequestStatus,
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
                                            },
                                            time = orderType.createdAt,
                                            orderStatus = null,
                                            isMaster = true
                                        )

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


