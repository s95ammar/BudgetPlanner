package com.s95ammar.budgetplanner.models.data

import androidx.annotation.IntDef

@IntDef(
    IntBudgetTransactionType.UNKNOWN,
    IntBudgetTransactionType.EXPENSE,
    IntBudgetTransactionType.INCOME
)
@Retention(AnnotationRetention.SOURCE)
annotation class IntBudgetTransactionType {
    companion object {
        const val UNKNOWN = 0
        const val EXPENSE = 1
        const val INCOME = 2
    }
}