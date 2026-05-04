package com.example.repairkz.ui.features.search.orderReg

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.repairkz.R
import com.example.repairkz.common.enums.PaymentMethod
import com.example.repairkz.common.extensions.toRussianString
import com.example.repairkz.common.ui.EnumDropDown
import com.example.repairkz.common.ui.ShortInfo
import com.example.repairkz.common.ui.ShortInput
import com.example.repairkz.common.ui.ShortWithComposableWOpadding
import com.example.repairkz.ui.features.components.CustomDatePicker
import com.example.repairkz.ui.features.components.CustomTimePicker

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OrderRegistration(
    orderRegistrationViewModel: OrderRegistrationViewModel = hiltViewModel(),
    navController: NavController,
) {

    val state by orderRegistrationViewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        orderRegistrationViewModel.channel.collect { effect ->
            when (effect) {
                OrderRegistrationEffects.NavigateBack -> {
                    navController.popBackStack()
                }
            }

        }
    }

    Scaffold(
        topBar = {
            Text(
                stringResource(R.string.placing),
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .statusBarsPadding()
            )

        },
        bottomBar = {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .navigationBarsPadding(),
                onClick = {
                    orderRegistrationViewModel.handleIntent(OrderRegistrationIntent.CreateOrderRequest)
                }
            ) {
                Text(stringResource(R.string.order_master))
            }
        }
    ) { paddingValues ->
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Column(
                Modifier.padding(horizontal = 8.dp)
            ) {
                Text(
                    stringResource(R.string.order_data),
                    style = MaterialTheme.typography.titleMedium.copy(
                        letterSpacing = 1.5.sp
                    ),
                    color = MaterialTheme.colorScheme.primary,
                )
                HorizontalDivider()
            }


            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                ) {
                Column(
                    modifier = Modifier.padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ShortInput(
                        titleResID = R.string.task,
                        value = state.description,
                        changeValue = {
                                newValue ->
                            orderRegistrationViewModel.handleIntent(
                                OrderRegistrationIntent.ChangeDescription(
                                    newValue
                                )
                            )
                        },
                        singleLine = false ,
                        leadingIcon = Icons.Default.Description
                    )
                    ShortInput(
                        titleResID = R.string.price,
                        value = state.price,
                        changeValue = { newValue ->
                            orderRegistrationViewModel.handleIntent(
                                OrderRegistrationIntent.ChangePrice(
                                    newValue
                                )
                            )
                        },
                        placeholderResId = R.string.price_holder,
                        textAlign = TextAlign.End,
                        suffix = { Text("₸") },
                        keyboardType = KeyboardType.Decimal,

                        )
                    ShortWithComposableWOpadding(
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
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                        ShortInfo(
                            R.string.selected_date,
                            state.date?.toRussianString() ?: stringResource(R.string.no_selected_date)
                        )
                        OutlinedButton(
                            onClick = {
                                orderRegistrationViewModel.handleIntent(OrderRegistrationIntent.OpenDayPicker)
                            }
                        ) {
                            Row(
                                modifier = Modifier,
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.EditCalendar, null
                                )
                                Spacer(modifier = Modifier.size(4.dp))
                                Text(stringResource(R.string.select_date))
                            }

                        }

                    }

                    if (state.isDayModalOpen) {
                        CustomDatePicker(
                            closePicker = {
                                orderRegistrationViewModel.handleIntent(OrderRegistrationIntent.CloseDayPicker)
                            },
                            selectedDate = { millis ->
                                orderRegistrationViewModel.handleIntent(
                                    OrderRegistrationIntent.ChangeDate(
                                        millis
                                    )
                                )
                            },
                            openTimePicker = {
                                orderRegistrationViewModel.handleIntent(OrderRegistrationIntent.OpenTimePicker)
                            }
                        )
                    }
                    if (state.isTimeModalOpen){
                        CustomTimePicker(
                            closePicker = {orderRegistrationViewModel.handleIntent(OrderRegistrationIntent.CloseTimePicker)},
                            selectedTime = { hour, minute ->
                                orderRegistrationViewModel.handleIntent(OrderRegistrationIntent.ChangeTime(hour, minute))
                                orderRegistrationViewModel.handleIntent(OrderRegistrationIntent.ChangeDateTime)
                            },
                        )
                    }

                }






            }



            Column(
                Modifier.padding(horizontal = 8.dp)
            ) {
                Text(
                    stringResource(R.string.contact_data),
                    style = MaterialTheme.typography.titleMedium.copy(
                        letterSpacing = 1.5.sp
                    ),
                    color = MaterialTheme.colorScheme.primary,
                )
                HorizontalDivider()
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),

                ){
                Column(
                    modifier = Modifier.padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ){
                    ShortInput(
                        titleResID = R.string.enter_address,
                        value = state.clientAddress,
                        changeValue = { newValue ->
                            orderRegistrationViewModel.handleIntent(
                                OrderRegistrationIntent.ChangeAddress(
                                    newValue
                                )
                            )
                        }
                    )
                    ShortInput(
                        titleResID = R.string.enter_number,
                        value = state.clientNumber,
                        changeValue = { newValue ->
                            orderRegistrationViewModel.handleIntent(
                                OrderRegistrationIntent.ChangeNumber(
                                    newValue
                                )
                            )
                        },
                        keyboardType = KeyboardType.Phone
                    )
                }

            }






        }
    }

}

