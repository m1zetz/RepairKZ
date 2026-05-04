package com.example.repairkz.domain.errors
import com.example.repairkz.R

import android.content.Context

sealed class AuthorizationError : Exception() {
    object NetworkError : AuthorizationError()
    object WrongCode : AuthorizationError()
    object WrongLoginOrPassword : AuthorizationError()
    object NoInternet : AuthorizationError()
    object TokenExpired : AuthorizationError()
}

fun AuthorizationError.toMessage(context: Context) : String{
    return when(this){
        AuthorizationError.NetworkError -> context.getString(R.string.network_error)
        AuthorizationError.NoInternet -> context.getString(R.string.no_internet)
        AuthorizationError.TokenExpired -> context.getString(R.string.token_expired)
        AuthorizationError.WrongCode -> context.getString(R.string.wrong_code)
        AuthorizationError.WrongLoginOrPassword -> context.getString(R.string.wrong_login_or_password)
    }
}