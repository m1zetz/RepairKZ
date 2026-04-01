package com.example.repairkz.common.enums
import com.example.repairkz.R
import androidx.annotation.StringRes
import com.example.repairkz.common.ui.DisplayableEnum

enum class PaymentMethod(@StringRes override val resID: Int) : DisplayableEnum {
    CARD(R.string.card),
    TRANSFER(R.string.transfer),
    CASH(R.string.cash),
    UNDEFINED (R.string.undefined)

}
