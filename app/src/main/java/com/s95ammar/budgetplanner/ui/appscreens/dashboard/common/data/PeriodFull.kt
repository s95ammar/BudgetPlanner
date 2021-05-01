package com.s95ammar.budgetplanner.ui.appscreens.dashboard.common.data

data class PeriodFull(
    val id: Int,
    val name: String,
    val max: Int?,
    val periodicCategories: List<PeriodicCategory>,
    val budgetTransactions: List<BudgetTransactionViewEntity>
)