package com.example.repairkz.common.utils

import androidx.annotation.StringRes
import com.example.repairkz.R
import com.example.repairkz.common.constants.EMAIL_ADDRESS_PATTERN
import kotlinx.coroutines.flow.MutableStateFlow


sealed class ValidationResult {
    object Success : ValidationResult()
    data class Error(@StringRes val messageRes: Int) : ValidationResult()
}

object Validator {
    fun validateEmail(email: String): ValidationResult {
        return when {
            email.isEmpty() -> ValidationResult.Error(R.string.email_empty)
            !EMAIL_ADDRESS_PATTERN.matcher(email)
                .matches() -> ValidationResult.Error(R.string.email_invalid_format)

            else -> ValidationResult.Success
        }
    }

    fun validatePassword(password: String): ValidationResult {
        return when {
            password.isEmpty() -> ValidationResult.Error(R.string.password_empty)
            password.length < 6 -> ValidationResult.Error(R.string.short_password)
            !password.contains("[0-9]".toRegex()) -> ValidationResult.Error(R.string.no_numbers)
            !password.contains("[A-Z]".toRegex()) -> ValidationResult.Error(R.string.no_uppercase)
            !password.contains("[a-z]".toRegex()) -> ValidationResult.Error(R.string.no_lowercase)
            else -> ValidationResult.Success
        }
    }
}
