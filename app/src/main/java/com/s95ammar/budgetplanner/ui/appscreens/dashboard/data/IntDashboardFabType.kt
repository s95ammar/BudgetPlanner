package com.s95ammar.budgetplanner.ui.appscreens.dashboard.data

import androidx.annotation.IntDef

@IntDef(
    IntDashboardFabType.FAB_EDIT,
    IntDashboardFabType.FAB_ADD
)
@Retention(AnnotationRetention.SOURCE)
annotation class IntDashboardFabType {
    companion object {
        const val FAB_NONE = 0
        const val FAB_EDIT = 1
        const val FAB_ADD = 2
    }
}
