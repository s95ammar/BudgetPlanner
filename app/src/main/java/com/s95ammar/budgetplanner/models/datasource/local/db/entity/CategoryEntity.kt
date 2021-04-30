package com.s95ammar.budgetplanner.models.datasource.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.s95ammar.budgetplanner.models.datasource.local.db.BudgetPlannerDbConfig

@Entity(tableName = BudgetPlannerDbConfig.TABLE_NAME_CATEGORY)
data class CategoryEntity(
    val name: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}