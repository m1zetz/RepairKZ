package com.example.repairkz.ui.features.search.orderReg

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.InvertColors
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.repairkz.Activity.ActivityIntent
import com.example.repairkz.R
import com.example.repairkz.common.enums.MasterSpetializationsEnum
import com.example.repairkz.common.enums.PaymentMethod
import com.example.repairkz.common.ui.EnumDropDown
import com.example.repairkz.common.ui.ShortInputCard
import com.example.repairkz.common.ui.ShortWithComposableCard
import com.example.repairkz.ui.features.UserInfo.UserIntent

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OrderRegistration(orderRegistrationViewModel: OrderRegistrationViewModel = hiltViewModel(), navController: NavController) {
    val state by orderRegistrationViewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        orderRegistrationViewModel.channel.collect {effect ->
            when(effect){
                OrderRegistrationEffects.NavigateBack -> {
                    navController.popBackStack()
                }
            }

        }
    }

    Surface(
        color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = Modifier.fillMaxSize()
            .padding(8.dp)) {

            Spacer(modifier = Modifier.size(52.dp))
            Text(stringResource(R.string.placing), fontSize = MaterialTheme.typography.titleLarge.fontSize)
            Spacer(modifier = Modifier.size(8.dp))
            HorizontalDivider(modifier = Modifier)
            Spacer(modifier = Modifier.size(4.dp))
            ShortInputCard(
                titleResID = R.string.task,
                value = state.description,
                changeValue = {newValue ->
                    orderRegistrationViewModel.handleIntent(OrderRegistrationIntent.ChangeDescription(newValue))
                },
                singleLine = false,
                leadingIcon = Icons.Default.Description
            )

            ShortInputCard(
                titleResID = R.string.price,
                value = state.price,
                changeValue = {newValue ->
                    orderRegistrationViewModel.handleIntent(OrderRegistrationIntent.ChangePrice(newValue))
                },
                placeholderResId = R.string.price_holder,
                textAlign = TextAlign.End,
                suffix = {Text("₸")},
                keyboardType = KeyboardType.Decimal,
                leadingIcon = Icons.Default.AttachMoney,

            )
            ShortWithComposableCard(
                R.string.payment_method,
                {
                    EnumDropDown(
                        R.string.choice_city,
                        state.paymentMethod,
                        PaymentMethod.entries,
                        onSelect = { method ->
                            orderRegistrationViewModel.handleIntent(
                                OrderRegistrationIntent.ChangePaymentMethod(
                                    method
                                )
                            )

                        }
                    )
                }
            )

            ShortInputCard(
                titleResID = R.string.address,
                value = state.clientAddress,
                changeValue = {newValue ->
                    orderRegistrationViewModel.handleIntent(OrderRegistrationIntent.ChangeAddress(newValue))
                }
            )
            ShortInputCard(
                titleResID = R.string.number,
                value = state.clientNumber,
                changeValue = {newValue ->
                    orderRegistrationViewModel.handleIntent(OrderRegistrationIntent.ChangeNumber(newValue))
                },
                keyboardType = KeyboardType.Phone
            )

            Button(
                modifier = Modifier.fillMaxWidth().padding(4.dp),
                onClick = {
                    orderRegistrationViewModel.handleIntent(OrderRegistrationIntent.CreateOrderRequest)
                }
            ) {
                Text(stringResource(R.string.order_master))
            }
        }
    }
}