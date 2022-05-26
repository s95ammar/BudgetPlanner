package com.s95ammar.budgetplanner.ui.common.data

import androidx.annotation.IntDef

@IntDef(
    IntCurrencySelectionType.MAIN_CURRENCY,
    IntCurrencySelectionType.CATEGORY_OF_PERIOD_CURRENCY,
    IntCurrencySelectionType.BUDGET_TRANSACTION_CURRENCY
)
@Retention(AnnotationRetention.SOURCE)
annotation class IntCurrencySelectionType {
    companion object {
        const val MAIN_CURRENCY = 1
        const val CATEGORY_OF_PERIOD_CURRENCY = 2
        const val BUDGET_TRANSACTION_CURRENCY = 3
    }
}
