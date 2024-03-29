package com.s95ammar.budgetplanner.models.datasource.local.db.entity.join

data class CategoryOfPeriodJoinEntity(
    val categoryOfPeriodId: Int,
    val periodId: Int,
    val categoryId: Int,
    val categoryName: String,
    val estimate: Double?,
    val currencyCode: String,
    val budgetTransactionsAmountSum: Double
)