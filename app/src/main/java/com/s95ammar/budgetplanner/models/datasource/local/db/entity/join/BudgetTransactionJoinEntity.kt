package com.s95ammar.budgetplanner.models.datasource.local.db.entity.join

import androidx.annotation.Nullable
import androidx.room.Embedded
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.LatLngEntity

data class BudgetTransactionJoinEntity(
    val id: Int,
    val name: String,
    val amount: Double,
    val currencyCode: String,
    @Nullable @Embedded val latLng: LatLngEntity?,
    val creationUnixMs: Long,
    val periodId: Int,
    val categoryOfPeriodId: Int,
    val categoryName: String
)