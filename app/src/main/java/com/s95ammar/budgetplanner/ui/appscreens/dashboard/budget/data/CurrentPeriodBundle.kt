package com.s95ammar.budgetplanner.ui.appscreens.dashboard.budget.data

import com.s95ammar.budgetplanner.models.view.PeriodViewEntity

data class CurrentPeriodBundle(
    val period: PeriodViewEntity?,
    val isPreviousAvailable: Boolean,
    val isNextAvailable: Boolean
)