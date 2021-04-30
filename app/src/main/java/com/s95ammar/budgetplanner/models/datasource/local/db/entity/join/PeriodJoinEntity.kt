package com.s95ammar.budgetplanner.models.datasource.local.db.entity.join

import com.s95ammar.budgetplanner.models.IntBudgetTransactionType

data class PeriodJoinEntity(
    // period
    val periodId: Int,
    val periodName: String,
    val periodMax: Int?,

    // periodicCategory
    val periodicCategoryId: Int,
    val categoryName: Int,
    val categoryId: Int,
    val periodicCategoryMax: Int?,
    val periodicCategoryBudgetTransactionsAmountSum: Int,

    // budgetTransaction
    val budgetTransactionId: Int,
    val budgetTransactionName: String,
    @IntBudgetTransactionType val budgetTransactionType: Int,
    val budgetTransactionsAmount: Int,
    val creationUnixMs: Long,
)