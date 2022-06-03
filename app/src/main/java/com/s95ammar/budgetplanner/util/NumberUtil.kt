package com.s95ammar.budgetplanner.util

import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat
import java.util.Locale

fun Double.roundToTwoDecimals(): Double {
    return BigDecimal(toString()).setScale(2, RoundingMode.HALF_UP).toDouble()
}

fun String?.toDoubleOrNull(locale: Locale): Double? {
    if (isNullOrEmpty()) return null
    return try {
        // try parse with default (US) locale first because of a potential Android keyboard bug, where the decimal separator can only be entered as a dot
        toDouble()
    } catch (e: Exception) {
        try {
            NumberFormat.getInstance(locale).parse(this)?.toDouble()
        } catch (e: Exception) {
            null
        }
    }
}

fun Int?.orDefault(defaultValue: Int): Int = this ?: defaultValue
fun Double?.orDefault(defaultValue: Double): Double = this ?: defaultValue
fun Float?.orDefault(defaultValue: Float): Float = this ?: defaultValue

fun Int?.orZero(): Int = this.orDefault(0)
fun Double?.orZero(): Double = this.orDefault(0.0)
fun Float?.orZero(): Float = this.orDefault(0.0f)