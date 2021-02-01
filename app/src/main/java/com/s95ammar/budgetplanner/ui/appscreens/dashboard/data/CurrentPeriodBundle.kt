package com.s95ammar.budgetplanner.ui.appscreens.dashboard.data

import com.s95ammar.budgetplanner.ui.appscreens.dashboard.common.data.PeriodSimpleViewEntity

data class CurrentPeriodBundle(
    val period: PeriodSimpleViewEntity?,
    val isPreviousAvailable: Boolean,
    val isNextAvailable: Boolean
)