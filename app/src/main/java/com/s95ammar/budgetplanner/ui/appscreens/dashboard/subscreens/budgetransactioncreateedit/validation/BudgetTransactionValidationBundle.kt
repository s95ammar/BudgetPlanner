package com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgetransactioncreateedit.validation

import com.s95ammar.budgetplanner.models.IntBudgetTransactionType
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.LatLngEntity

data class BudgetTransactionValidationBundle(
    @IntBudgetTransactionType val type: Int,
    val name: String,
    val amount: String,
    val periodicCategoryId: Int,
    val latLng: LatLngEntity? = null // TODO BP102
)