package com.example.repairkz.common.enums

import androidx.annotation.StringRes
import com.example.repairkz.R

enum class StatusOfUser(@StringRes val resID: Int){
    CLIENT(R.string.client),
    MASTER(R.string.master)
}