package com.example.repairkz.domain.errors

import android.content.Context
import com.example.repairkz.R

sealed class SettingsError(
){
    object UserIsNotRegistered : SettingsError()
    object ChangeStatusError : SettingsError()


}
fun SettingsError.toMessage(context: Context) : String{
    return when(this){
        SettingsError.ChangeStatusError -> context.getString(R.string.status_change_error)
        SettingsError.UserIsNotRegistered -> context.getString(R.string.you_not_authorized)
    }
}