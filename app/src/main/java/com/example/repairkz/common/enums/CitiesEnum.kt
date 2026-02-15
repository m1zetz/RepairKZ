package com.example.repairkz.common.enums

import com.example.repairkz.R
import androidx.annotation.StringRes
import com.example.repairkz.common.ui.DisplayableEnum

enum class CitiesEnum(@StringRes override val resID: Int) : DisplayableEnum {
    KARAGANDY(R.string.Karagandy),
    ALMATY(R.string.Almaty),
    ASTANA(R.string.Astana),
    SHYMKENT(R.string.Shymkent),
}