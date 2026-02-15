package com.example.repairkz.common.enums
import com.example.repairkz.R
import androidx.annotation.StringRes
import com.example.repairkz.common.ui.DisplayableEnum

enum class MasterSpetializationsEnum(@StringRes override val resID: Int) : DisplayableEnum {
    ELECTRIC(R.string.electrician),
    PLUMBER(R.string.plumber),
    HANDYMAN(R.string.handyman),
    СOMPUTER(R.string.computer),
    FURNITURE(R.string.furniture),
}