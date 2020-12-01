package com.s95ammar.budgetplanner.models.view

import com.s95ammar.budgetplanner.models.common.IntBudgetTransactionType

data class BudgetTransactionViewEntity(
    val id: Int,
    val name: String,
    @IntBudgetTransactionType val type: Int,
    val amount: Int,
    val creationUnixMs: Long
)