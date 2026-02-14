package com.example.repairkz.ui.features.components

import android.os.Build
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


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Details(orderData: Order){
    Column(
        modifier = Modifier.fillMaxSize()
            .padding(8.dp)
            .verticalScroll(rememberScrollState())
    ) {
        ShortInfoCard(R.string.masterSpecialization, orderData.masterSpecialization)
        ShortInfoCard(R.string.masterName, orderData.masterName)
        ShortInfoCard(R.string.time, "${orderData.time.toRussianString()} в ${orderData.time.hour}:${orderData.time.minute}")
        ShortInfoCard(R.string.description, orderData.description)
        ShortInfoCard(R.string.cost, "${orderData.cost} ₸")
        val paymentText = when(orderData.paymentMethod) {
            PaymentMethod.CASH -> stringResource(R.string.payment_cash)
            PaymentMethod.CARD -> stringResource(R.string.payment_card)
        }
        ShortInfoCard(R.string.payment_system, paymentText)
    }
}

