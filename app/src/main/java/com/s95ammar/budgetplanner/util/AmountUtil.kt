package com.s95ammar.budgetplanner.util

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.s95ammar.budgetplanner.R
import kotlin.math.roundToLong

fun Context.getAmountStringFormatted(
    amount: Double,
    isForEditText: Boolean = false,
    removeDecimalsIfWhole: Boolean = true,
    includePlusSign: Boolean = true,
    currencyCode: String? = null
) : String {

    val isWhole = amount == amount.roundToLong().toDouble()
    val amountFormatResId = if (removeDecimalsIfWhole && isWhole) {
        if (isForEditText) {
            R.string.format_amount_no_decimals_edit_text
        } else {
            if (includePlusSign) {
                R.string.format_amount_no_decimals_signed_with_thousand_separator
            } else {
                R.string.format_amount_no_decimals_with_thousand_separator
            }
        }
    } else {
        if (isForEditText) {
            R.string.format_amount_two_decimals_edit_text
        } else {
            if (includePlusSign) {
                R.string.format_amount_two_decimals_signed_with_thousand_separator
            } else {
                R.string.format_amount_two_decimals_with_thousand_separator
            }
        }
    }

    val amountFormatted = getString(amountFormatResId, amount)

    return currencyCode?.takeUnless { isForEditText }?.let { code ->
        getString(R.string.format_amount_string_with_currency, amountFormatted, code)
    } ?: amountFormatted
}

fun RecyclerView.ViewHolder.getAmountStringFormatted(
    amount: Double,
    isForEditText: Boolean = false,
    removeDecimalsIfWhole: Boolean = true,
    includePlusSign: Boolean = true,
    currencyCode: String? = null
) : String {
    return itemView.context.getAmountStringFormatted(
        amount,
        isForEditText,
        removeDecimalsIfWhole,
        includePlusSign,
        currencyCode
    )
}

fun Fragment.getAmountStringFormatted(
    amount: Double,
    isForEditText: Boolean = false,
    removeDecimalsIfWhole: Boolean = true,
    includePlusSign: Boolean = true,
    currencyCode: String? = null
) : String {
    return requireContext().getAmountStringFormatted(
        amount,
        isForEditText,
        removeDecimalsIfWhole,
        includePlusSign,
        currencyCode
    )
}
