package com.example.repairkz.ui.features.components

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.repairkz.R
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.repairkz.common.enums.PaymentMethod
import com.example.repairkz.common.extensions.toRussianString
import com.example.repairkz.common.models.Order
import com.example.repairkz.common.ui.ShortInfoCard
import com.example.repairkz.ui.features.notifiacton.HistoryItem
import java.time.LocalDateTime


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Details(data: HistoryItem){
    when(data){
        is HistoryItem.ClientItem -> {
            val orderData = data.data
            Column(
                modifier = Modifier.fillMaxSize()
                    .padding(8.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                ShortInfoCard(R.string.masterSpecialization, stringResource(orderData.masterSpec.resID))
                ShortInfoCard(R.string.masterName, orderData.masterFirstName)
                val dateTime = orderData.orderDate?.let { text -> LocalDateTime.parse(text) }
                val dateString = dateTime?.toRussianString() ?: ""
                val timeString = "${dateTime?.hour}:${dateTime?.minute}"
                ShortInfoCard(R.string.time, "$dateString в $timeString")
                ShortInfoCard(R.string.description, orderData.description?:"")
                ShortInfoCard(R.string.cost, "${orderData.offeredPrice?:0} ₸")
                val paymentText = when(orderData.paymentMethod) {
                    PaymentMethod.CASH -> stringResource(R.string.payment_cash)
                    PaymentMethod.CARD -> stringResource(R.string.payment_card)
                    PaymentMethod.TRANSFER -> stringResource(R.string.transfer)
                    PaymentMethod.UNDEFINED -> stringResource(R.string.undefined)
                    null -> {
                        ""
                    }
                }
                ShortInfoCard(R.string.payment_system, paymentText)
                Log.d("Payment", "${orderData.paymentMethod}")
            }
        }
        is HistoryItem.MasterItem -> {
            val orderData = data.data
            Column(
                modifier = Modifier.fillMaxSize()
                    .padding(8.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                ShortInfoCard(R.string.clientName, "${orderData.clientFirstName} ${orderData.clientLastName}")
                val dateTime = orderData.orderDate?.let { text -> LocalDateTime.parse(text) }
                val dateString = dateTime?.toRussianString() ?: ""
                val timeString = "${dateTime?.hour}:${dateTime?.minute}"
                ShortInfoCard(R.string.time, "$dateString в $timeString")
                ShortInfoCard(R.string.address, orderData.clientAddress ?: "")
                ShortInfoCard(R.string.number, orderData.clientPhoneNumber ?: "")
                ShortInfoCard(R.string.description, orderData.description ?: "")
                ShortInfoCard(R.string.cost, "${orderData.offeredPrice ?: 0} ₸")
                val paymentText = when(orderData.paymentMethod) {
                    PaymentMethod.CASH -> stringResource(R.string.payment_cash)
                    PaymentMethod.CARD -> stringResource(R.string.payment_card)
                    PaymentMethod.TRANSFER -> stringResource(R.string.transfer)
                    PaymentMethod.UNDEFINED -> stringResource(R.string.undefined)
                    null -> {
                        ""
                    }
                }
                ShortInfoCard(R.string.payment_system, paymentText)
            }
        }
    }

}

