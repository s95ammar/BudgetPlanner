package com.s95ammar.budgetplanner.models.view

data class PeriodViewEntity(
    val id: Int,
    val name: String,
    val max: Int?,
    val periodRecords: List<PeriodRecordViewEntity>,
    val budgetTransactions: List<BudgetTransactionViewEntity>
)