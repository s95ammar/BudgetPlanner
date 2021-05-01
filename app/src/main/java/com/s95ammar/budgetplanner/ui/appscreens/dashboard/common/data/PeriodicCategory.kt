package com.s95ammar.budgetplanner.ui.appscreens.dashboard.common.data

data class PeriodicCategory(
    val categoryName: String,
    val categoryId: Int,
    var max: Int?,
    val amount: Int,
    val isSelected: Boolean
)