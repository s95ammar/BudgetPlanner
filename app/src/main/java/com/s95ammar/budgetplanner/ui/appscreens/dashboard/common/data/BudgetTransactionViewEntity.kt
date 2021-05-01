package com.s95ammar.budgetplanner.ui.appscreens.dashboard.common.data

import com.s95ammar.budgetplanner.models.IntBudgetTransactionType

data class BudgetTransactionViewEntity(
    val id: Int,
    val name: String,
    @IntBudgetTransactionType val type: Int,
    val amount: Int,
    val creationUnixMs: Long,
    val periodicCategoryId: Int,
    val categoryName: String
)