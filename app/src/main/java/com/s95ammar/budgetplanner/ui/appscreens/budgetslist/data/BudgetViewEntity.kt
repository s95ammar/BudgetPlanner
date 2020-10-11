package com.s95ammar.budgetplanner.ui.appscreens.budgetslist.data

data class BudgetViewEntity(
    val id: Int,
    val name: String,
    val isActive: Boolean,
    val totalBalance: Long,
    val totalSpendingEstimate: Long?,
    val totalSavings: Long?
)