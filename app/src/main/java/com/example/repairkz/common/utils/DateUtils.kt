package com.example.repairkz.common.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalTime

import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
fun formattedTime(hour: Int, minute: Int): String {
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    val time = LocalTime.of(hour, minute).format(formatter)
    return time
}