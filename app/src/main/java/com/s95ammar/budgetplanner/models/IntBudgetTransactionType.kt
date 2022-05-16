package com.s95ammar.budgetplanner.models

import androidx.annotation.ColorRes
import androidx.annotation.IntDef
import com.s95ammar.budgetplanner.R

@Target(AnnotationTarget.CLASS, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FUNCTION, AnnotationTarget.TYPE)
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

        fun getByPosition(position: Int) = values()[position]
        fun getPosition(@IntBudgetTransactionType type: Int) = values().indexOf(type)
        fun getByAmount(amount: Double) = if (amount <= 0) EXPENSE else INCOME
        @ColorRes
        fun getColorRes(type: @IntBudgetTransactionType Int): Int {
            return if (type == EXPENSE) R.color.colorRed else R.color.colorGreen
        }
    }
}

fun Double.isExpense() = IntBudgetTransactionType.getByAmount(this) == IntBudgetTransactionType.EXPENSE