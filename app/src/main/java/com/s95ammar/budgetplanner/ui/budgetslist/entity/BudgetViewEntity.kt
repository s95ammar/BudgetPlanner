package com.s95ammar.budgetplanner.ui.budgetslist.entity

data class BudgetViewEntity(
    val id: Int,
    val name: String,
    val totalBalance: Long,
    val totalSpendingEstimate: Long,
    val totalSavings: Long
)