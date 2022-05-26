package com.s95ammar.budgetplanner.ui.appscreens.dashboard.common.data

data class PeriodWithCategoriesOfPeriod(
    val periodId: Int,
    val periodName: String?,
    val categoriesOfPeriod: List<CategoryOfPeriod>
)
