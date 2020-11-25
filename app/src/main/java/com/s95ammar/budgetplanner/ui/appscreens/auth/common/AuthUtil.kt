package com.s95ammar.budgetplanner.ui.appscreens.auth.common

import android.util.Patterns
import java.util.regex.Pattern

object AuthUtil {
    const val PATTERN_VALID_PASSWORD_8_DIGITS = "(?=.*[a-zA-Z0-9]).{8,}$"
    const val PASSWORD_LENGTH_8_DIGITS = 8

    fun isEmailValid(email: String) = Patterns.EMAIL_ADDRESS.matcher(email).matches()

    fun isPasswordValid(password: String) = Pattern.compile(PATTERN_VALID_PASSWORD_8_DIGITS).matcher(password).matches()
}