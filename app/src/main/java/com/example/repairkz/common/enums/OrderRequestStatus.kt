package com.example.repairkz.common.enums

import androidx.annotation.StringRes
import com.example.repairkz.R
import com.example.repairkz.common.ui.DisplayableEnum

enum class OrderRequestStatus(@StringRes override val resID: Int ) : DisplayableEnum
{
    ACCEPTED(R.string.accepted),
    REJECTED(R.string.rejected),
    PENDING(R.string.pending),
}