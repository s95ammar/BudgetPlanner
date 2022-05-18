package com.s95ammar.budgetplanner.ui.appscreens.dashboard.pager

import androidx.annotation.IntDef

@Target(AnnotationTarget.CLASS, AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.TYPE)
@IntDef(
    IntDashboardTab.TAB_BUDGET,
    IntDashboardTab.TAB_BUDGET_TRANSACTIONS,
    IntDashboardTab.TAB_SAVINGS
)
@Retention(AnnotationRetention.SOURCE)
annotation class IntDashboardTab {
    companion object {
        const val TAB_BUDGET = 0
        const val TAB_BUDGET_TRANSACTIONS = 1
        const val TAB_SAVINGS = 2

        fun values() = listOf(TAB_BUDGET, TAB_BUDGET_TRANSACTIONS, TAB_SAVINGS)
    }
}
