package com.s95ammar.budgetplanner.models.datasource.local.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.s95ammar.budgetplanner.models.datasource.local.db.BudgetPlannerDbConfig

@Entity(
    tableName = BudgetPlannerDbConfig.TABLE_NAME_CATEGORY,
    indices = [Index(BudgetPlannerDbConfig.COLUMN_NAME_NAME, unique = true)]
)
data class CategoryEntity(
    @ColumnInfo(collate = ColumnInfo.NOCASE) val name: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
