package com.s95ammar.budgetplanner.models.api.responses

data class PeriodApiEntity(
    val id: Int?,
    val name: String?,
    val max: Int?,
    val periodicCategories: List<PeriodicCategoryApiEntity?>?,
    val budgetTransactions: List<BudgetTransactionApiEntity>?,
)