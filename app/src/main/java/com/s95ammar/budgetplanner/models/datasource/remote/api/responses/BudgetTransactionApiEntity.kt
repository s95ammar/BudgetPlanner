package com.s95ammar.budgetplanner.models.datasource.remote.api.responses

import com.s95ammar.budgetplanner.models.IntBudgetTransactionType

data class BudgetTransactionApiEntity(
    val id: Int?,
    val name: String?,
    @IntBudgetTransactionType val type: Int?,
    val amount: Int?,
    val creationUnixMs: Long?,
    val periodicCategoryId: Int?,
    val categoryName: String?,
    val userId: Int?
)