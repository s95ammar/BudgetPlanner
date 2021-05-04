package com.s95ammar.budgetplanner.ui.appscreens.dashboard.pager

import androidx.annotation.IntDef

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
    }
}
