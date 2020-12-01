package com.s95ammar.budgetplanner.models.common

@Retention(AnnotationRetention.SOURCE)
annotation class IntBudgetTransactionType {
    companion object {
        const val EXPENSE = 1
        const val INCOME = 2
    }
}