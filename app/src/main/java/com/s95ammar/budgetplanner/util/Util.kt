package com.s95ammar.budgetplanner.util

import android.content.Context
import java.util.Locale

fun Boolean?.orFalse() = this ?: false

const val INT_INVALID = -1

val Int.Companion.INVALID
    get() = INT_INVALID

@Suppress(WARNING_DEPRECATION)
val Context.currentLocale: Locale
    get() = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
        resources.configuration.locales[0]
    } else {
        resources.configuration.locale
    }

const val WARNING_DEPRECATION = "DEPRECATION"
const val WARNING_UNNECESSARY_SAFE_CALL = "UNNECESSARY_SAFE_CALL"
const val WARNING_UNCHECKED_CAST = "UNCHECKED_CAST"

const val FAB_VISIBILITY_ANIMATION_DURATION_MS = 150L

const val COLOR_PRIMARY_HEX = "#6200Ee"
