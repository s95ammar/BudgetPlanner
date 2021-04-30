package com.s95ammar.budgetplanner.models.datasource.remote.api.requests

import com.s95ammar.budgetplanner.models.IntBudgetTransactionType

sealed class BudgetTransactionUpsertApiRequest {

    data class Insertion(
        val name: String?,
        @IntBudgetTransactionType val type: Int?,
        val amount: Int?,
        val periodicCategoryId: Int?
    ) : BudgetTransactionUpsertApiRequest()

    data class Update(
        val id: Int?,
        val name: String?,
        @IntBudgetTransactionType val type: Int?,
        val amount: Int?,
        val periodicCategoryId: Int?
    ) : BudgetTransactionUpsertApiRequest()

}