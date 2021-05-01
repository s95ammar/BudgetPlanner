package com.s95ammar.budgetplanner.models.datasource.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.s95ammar.budgetplanner.models.datasource.local.db.BudgetPlannerDbConfig

@Entity(tableName = BudgetPlannerDbConfig.TABLE_NAME_PERIOD)
data class PeriodEntity(
    val name: String,
    val max: Int?,
    val creationUnixMs: Long = System.currentTimeMillis()
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
