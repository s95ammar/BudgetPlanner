package com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgetransactioncreateedit.validation

import com.s95ammar.budgetplanner.models.IntBudgetTransactionType

data class BudgetTransactionValidationBundle(
    @IntBudgetTransactionType val type: Int,
    val name: String,
    val amount: String,
    val periodicCategoryId: Int
)