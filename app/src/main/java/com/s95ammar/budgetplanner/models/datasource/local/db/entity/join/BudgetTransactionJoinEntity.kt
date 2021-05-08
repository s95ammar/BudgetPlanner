package com.s95ammar.budgetplanner.models.datasource.local.db.entity.join

import com.s95ammar.budgetplanner.models.IntBudgetTransactionType

data class BudgetTransactionJoinEntity(
    val id: Int,
    val name: String,
    @IntBudgetTransactionType val type: Int,
    val amount: Int,
    val creationUnixMs: Long,
    val periodicCategoryId: Int,
    val categoryName: String
)