package com.example.repairkz.common.extensions

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDateTime.toRussianString(): String {
    val formatter = DateTimeFormatter.ofPattern("d MMMM", Locale("ru"))
    return this.format(formatter)
}