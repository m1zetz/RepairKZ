package com.example.repairkz.ui.features.components
import android.widget.TimePicker
import com.example.repairkz.R

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerLayoutType
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDatePicker(
    closePicker: () -> Unit,
    selectedDate: (Long) -> Unit,
    openTimePicker: () -> Unit
    ) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(onDismissRequest = { closePicker() }, confirmButton = {
        TextButton(
            onClick = {
                selectedDate(datePickerState.selectedDateMillis ?: 0)
                closePicker()
                openTimePicker()
            }) {
            Text(stringResource(R.string.select_time), color = MaterialTheme.colorScheme.primary)
        }
    }, dismissButton = {
        TextButton(
            onClick = { closePicker() }) {
            Text(stringResource(R.string.cancel), color = MaterialTheme.colorScheme.primary)
        }
    }) {
        DatePicker(
            state = datePickerState, modifier = Modifier.sizeIn(maxWidth = 350.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTimePicker(
    closePicker: () -> Unit,
    selectedTime: (hour: Int, minute: Int) -> Unit
) {
    val timeState = rememberTimePickerState(
        is24Hour = true
    )

    Dialog(
        onDismissRequest = { closePicker() },
        properties = DialogProperties(usePlatformDefaultWidth = true)
    ){
        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                text = "Select time"
            )
            TimePicker(
                state = timeState,
                layoutType = TimePickerLayoutType.Vertical,
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    modifier = Modifier.padding(end = 8.dp),
                    onClick = { closePicker() }
                ) {
                    Text(
                        text = stringResource(R.string.cancel)
                    )
                }
                Button(
                    modifier = Modifier.padding(start = 8.dp),
                    onClick = {
                        selectedTime(timeState.hour, timeState.minute)
                        closePicker()
                    }
                ) {
                    Text(text = stringResource(R.string.OK))
                }
            }
        }
    }


}