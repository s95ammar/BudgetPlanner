package com.s95ammar.budgetplanner.util

import java.math.BigDecimal
import java.math.RoundingMode

fun Double.roundToTwoDecimals(): Double {
    return BigDecimal(toString()).setScale(2, RoundingMode.HALF_UP).toDouble()
}

fun Int?.orZero(): Int = this ?: 0
fun Double?.orZero(): Double = this ?: 0.0
fun Float?.orZero(): Float = this ?: 0.0f