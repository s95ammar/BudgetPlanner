package com.s95ammar.budgetplanner.models.datasource.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.s95ammar.budgetplanner.models.datasource.local.db.BudgetPlannerDbConfig

@Entity(
    tableName = BudgetPlannerDbConfig.TABLE_NAME_CURRENCY
)
data class CurrencyEntity(
    @PrimaryKey
    val code: String,
    val name: String
)
