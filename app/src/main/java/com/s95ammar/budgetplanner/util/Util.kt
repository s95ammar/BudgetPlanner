package com.s95ammar.budgetplanner.util

import android.content.Context
import java.util.*

fun Boolean?.orFalse() = this ?: false

const val INT_INVALID = -1

val Int.Companion.INVALID
    get() = INT_INVALID

@Suppress("Deprecation")
val Context.currentLocale: Locale
    get() = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
        resources.configuration.locales[0]
    } else {
        resources.configuration.locale
    }