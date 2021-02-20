package com.s95ammar.budgetplanner.models.api.responses

import androidx.annotation.IntDef

@IntDef(
    IntBudgetTransactionType.EXPENSE,
    IntBudgetTransactionType.INCOME
)
@Retention(AnnotationRetention.SOURCE)
annotation class IntBudgetTransactionType {
    companion object {
        const val EXPENSE = 1
        const val INCOME = 2

        fun values() = listOf(
            EXPENSE,
            INCOME
        )
    }
}