package com.s95ammar.budgetplanner.models.datasource.local.db.entity.join

import androidx.annotation.Nullable
import androidx.room.Embedded
import com.s95ammar.budgetplanner.models.IntBudgetTransactionType
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.LatLngEntity

data class BudgetTransactionJoinEntity(
    val id: Int,
    val name: String,
    @IntBudgetTransactionType val type: Int,
    val amount: Int,
    @Nullable @Embedded val latLng: LatLngEntity?,
    val creationUnixMs: Long,
    val periodId: Int,
    val periodicCategoryId: Int,
    val categoryName: String
)