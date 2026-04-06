package com.example.repairkz.common.enums

import androidx.annotation.StringRes
import com.example.repairkz.R
import com.example.repairkz.common.ui.DisplayableEnum

enum class OrderStatus(@StringRes override val resID: Int) : DisplayableEnum{
    RUNNING(R.string.running),
    COMPLETED(R.string.completed)
}