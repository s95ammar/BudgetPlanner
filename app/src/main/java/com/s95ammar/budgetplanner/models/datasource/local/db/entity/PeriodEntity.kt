package com.s95ammar.budgetplanner.models.datasource.local.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.s95ammar.budgetplanner.models.datasource.local.db.BudgetPlannerDbConfig

@Entity(
    tableName = BudgetPlannerDbConfig.TABLE_NAME_PERIOD,
    indices = [Index(BudgetPlannerDbConfig.COLUMN_NAME_NAME, unique = true)]
)
data class PeriodEntity(
    @ColumnInfo(collate = ColumnInfo.NOCASE) val name: String,
    val creationUnixMs: Long = System.currentTimeMillis()
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
