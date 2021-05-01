package com.s95ammar.budgetplanner.ui.appscreens.dashboard.data

import com.s95ammar.budgetplanner.ui.appscreens.dashboard.common.data.PeriodSimple

data class CurrentPeriodHeaderBundle(
    val period: PeriodSimple?,
    val isPreviousAvailable: Boolean,
    val isNextAvailable: Boolean
)