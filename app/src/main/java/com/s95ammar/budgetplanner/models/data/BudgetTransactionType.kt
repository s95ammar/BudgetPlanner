package com.s95ammar.budgetplanner.models.data

import androidx.annotation.IntDef

const val UNKNOWN = 0
const val EXPENSE = 1
const val INCOME = 2

@IntDef(
    UNKNOWN,
    EXPENSE,
    INCOME
)
@Retention(AnnotationRetention.SOURCE)
annotation class IntBudgetTransactionType