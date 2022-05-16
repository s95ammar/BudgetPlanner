package com.s95ammar.budgetplanner.util

import androidx.annotation.StringRes
import com.s95ammar.budgetplanner.R
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.roundToInt

@StringRes
fun getAmountFormatResId(
    amount: Double,
    isForEditText: Boolean = false,
    removeDecimalsIfWhole: Boolean = true,
    includePlusSign: Boolean = true
): Int {
    val isWhole = amount == amount.roundToInt().toDouble()
    return if (removeDecimalsIfWhole && isWhole) {
        if (isForEditText) {
            R.string.format_budget_transaction_amount_no_decimals_edit_text
        } else {
            if (includePlusSign) {
                R.string.format_budget_transaction_amount_no_decimals_signed
            } else {
                R.string.format_budget_transaction_amount_no_decimals
            }
        }
    } else {
        if (isForEditText) {
            R.string.format_budget_transaction_amount_two_decimals_edit_text
        } else {
            if (includePlusSign) {
                R.string.format_budget_transaction_amount_two_decimals_signed
            } else {
                R.string.format_budget_transaction_amount_two_decimals
            }
        }
    }
}

fun Double.roundToTwoDecimals(): Double {
    return BigDecimal(toString()).setScale(2, RoundingMode.HALF_UP).toDouble()
}

fun Int?.orZero(): Int = this ?: 0
fun Double?.orZero(): Double = this ?: 0.0
fun Float?.orZero(): Float = this ?: 0.0f