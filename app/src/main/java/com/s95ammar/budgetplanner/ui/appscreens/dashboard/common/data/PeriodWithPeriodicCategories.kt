package com.s95ammar.budgetplanner.ui.appscreens.dashboard.common.data

data class PeriodWithPeriodicCategories(
    val periodId: Int,
    val periodName: String?,
    val periodicCategories: List<PeriodicCategory>
)
