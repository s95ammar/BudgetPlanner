package com.s95ammar.budgetplanner.ui.appscreens.dashboard.data

import com.s95ammar.budgetplanner.ui.appscreens.dashboard.pager.IntDashboardTab

data class DashboardFabState(
    @IntDashboardFabType val type: Int,
    @IntDashboardTab val currentTab: Int
)
