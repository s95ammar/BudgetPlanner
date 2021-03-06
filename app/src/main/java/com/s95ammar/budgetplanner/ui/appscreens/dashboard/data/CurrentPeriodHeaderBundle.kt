package com.s95ammar.budgetplanner.ui.appscreens.dashboard.data

import com.s95ammar.budgetplanner.ui.appscreens.dashboard.common.data.PeriodSimpleViewEntity

data class CurrentPeriodHeaderBundle(
    val period: PeriodSimpleViewEntity?,
    val isPreviousAvailable: Boolean,
    val isNextAvailable: Boolean
)