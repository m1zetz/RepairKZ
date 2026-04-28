package com.example.repairkz.ui.features.notifiacton


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.repairkz.R
import com.example.repairkz.common.enums.OrderRequestStatus
import com.example.repairkz.common.enums.OrderStatus
import com.example.repairkz.ui.theme.darkAccept
import com.example.repairkz.ui.theme.darkReject
import com.example.repairkz.ui.theme.pendingOrange
import com.example.repairkz.ui.theme.white
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NotifyCard(
    text: String,
    intent: () -> Unit,
    accept: (() -> Unit)? = null,
    reject: (() -> Unit)? = null,
    color: Color = MaterialTheme.colorScheme.onSurface,
    orderStatus: OrderStatus?,
    changeOrderStatus: (() -> Unit)? = null,
    time: LocalDateTime? = null,
    requestStatus: OrderRequestStatus?,
    isMaster: Boolean = false,
) {
    val cardWidth = if (isMaster && requestStatus == OrderRequestStatus.PENDING) 325.dp else 260.dp
    val cardPosition = if (!isMaster) Alignment.End else Alignment.Start
    val interactionSource = remember { MutableInteractionSource() }
    Column(modifier = Modifier.fillMaxWidth()) {
        Card(
            modifier = Modifier
                .widthIn(max = cardWidth)
                .padding(horizontal = 8.dp, vertical = 8.dp)
                .align(cardPosition),
            shape = RoundedCornerShape(
                topStart = 25.dp,
                topEnd = 25.dp,
                bottomStart = if (isMaster) 5.dp else 25.dp,
                bottomEnd = if (isMaster) 25.dp else 5.dp
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
                Column {
                    val headerText = when {
                        orderStatus == OrderStatus.RUNNING -> {
                            R.string.active_order
                        }

                        orderStatus == OrderStatus.COMPLETED -> {
                            R.string.completed
                        }

                        orderStatus == OrderStatus.REJECTED -> {
                            R.string.rejected
                        }

                        isMaster && requestStatus != null -> R.string.pending
                        else -> null

                    }
                    if (headerText != null) {
                        val headerColor = when (orderStatus) {
                            OrderStatus.RUNNING -> {
                                MaterialTheme.colorScheme.primary
                            }

                            OrderStatus.COMPLETED -> {
                                MaterialTheme.colorScheme.outline
                            }

                            OrderStatus.REJECTED -> {
                                com.example.repairkz.ui.theme.reject
                            }

                            else -> pendingOrange
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(headerColor)
                            )
                            Text(stringResource(headerText), color = headerColor)
                        }
                        HorizontalDivider()
                    }

                }



                Text(
                    text = text,
                    color = color
                )
                if (isMaster && requestStatus == OrderRequestStatus.PENDING) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Button(
                            modifier = Modifier
                                .padding(4.dp).weight(1f),
                            onClick = {
                                accept?.invoke()
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            ),
                            shape = RoundedCornerShape(9.dp),
                            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 0.dp)

                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Check, contentDescription = null, tint = white)
                                Spacer(Modifier.width(4.dp))
                                Text(stringResource(R.string.to_accept), color = white)
                            }


                        }
                        OutlinedButton(
                            modifier = Modifier
                                .padding(4.dp).weight(1f),
                            onClick = {
                                reject?.invoke()
                            },

                            shape = RoundedCornerShape(9.dp),
                            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 0.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Close, contentDescription = null, tint = white)
                                Spacer(Modifier.width(4.dp))
                                Text(stringResource(R.string.to_reject), color = white)
                            }

                        }
                    }

                }
                when (orderStatus) {
                    OrderStatus.RUNNING -> {
                        if (changeOrderStatus != null) {
                            Button(
                                modifier = Modifier.fillMaxWidth(),
                                onClick = {
                                    changeOrderStatus()
                                },
                                shape = RoundedCornerShape(9.dp)
                            ) {
                                Text(stringResource(R.string.complete_the_order))
                            }
                        }

                    }

                    else -> {}
                }
                Text(
                    stringResource(R.string.details),
                    textDecoration = TextDecoration.Underline,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        )
                        {
                            intent()
                        }

                )
                time?.let {
                    Text(
                        "%02d:%02d".format(time.hour, time.minute),
                        fontSize = 12.sp,
                        textAlign = TextAlign.End,
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

        }
    }

}

