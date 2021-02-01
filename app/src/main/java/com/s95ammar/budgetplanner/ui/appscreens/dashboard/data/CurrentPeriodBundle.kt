package com.s95ammar.budgetplanner.ui.appscreens.dashboard.data

import com.s95ammar.budgetplanner.models.view.PeriodSimpleViewEntity

data class CurrentPeriodBundle(
    val period: PeriodSimpleViewEntity?,
    val isPreviousAvailable: Boolean,
    val isNextAvailable: Boolean
)