package com.s95ammar.budgetplanner.models.datasource.local.db.entity.join

import com.s95ammar.budgetplanner.models.IntBudgetTransactionType

data class PeriodPeriodicCategoryBudgetTransactionJoinEntity(

    val budgetTransactionId: Int,
    val budgetTransactionName: String,
    @IntBudgetTransactionType val budgetTransactionType: Int,
    
)