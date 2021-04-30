package com.s95ammar.budgetplanner.models.datasource.local.db.entity.join

data class PeriodicCategoryJoinEntity(
    val periodicCategoryId: Int,
    val periodId: Int,
    val categoryName: Int,
    val categoryId: Int,
    val max: Int?,
    val budgetTransactionsAmountSum: Int
)