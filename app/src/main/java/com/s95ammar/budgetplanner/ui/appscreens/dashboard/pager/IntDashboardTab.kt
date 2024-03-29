package com.s95ammar.budgetplanner.ui.appscreens.dashboard.pager

import androidx.annotation.IntDef

@Target(AnnotationTarget.CLASS, AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.TYPE)
@IntDef(
    IntDashboardTab.TAB_OVERVIEW,
    IntDashboardTab.TAB_BUDGET_TRANSACTIONS,
)
@Retention(AnnotationRetention.SOURCE)
annotation class IntDashboardTab {
    companion object {
        const val TAB_OVERVIEW = 0
        const val TAB_BUDGET_TRANSACTIONS = 1

        fun values() = listOf(TAB_OVERVIEW, TAB_BUDGET_TRANSACTIONS)
    }
}
