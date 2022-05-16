package com.s95ammar.budgetplanner.models.datasource.local.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.s95ammar.budgetplanner.models.datasource.local.db.BudgetPlannerDbConfig

@Entity(
    tableName = BudgetPlannerDbConfig.TABLE_NAME_PERIODIC_CATEGORY,
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = [BudgetPlannerDbConfig.COLUMN_NAME_ID],
            childColumns = [BudgetPlannerDbConfig.COLUMN_NAME_CATEGORY_ID],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = PeriodEntity::class,
            parentColumns = [BudgetPlannerDbConfig.COLUMN_NAME_ID],
            childColumns = [BudgetPlannerDbConfig.COLUMN_NAME_PERIOD_ID],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = [BudgetPlannerDbConfig.COLUMN_NAME_CATEGORY_ID]),
        Index(value = [BudgetPlannerDbConfig.COLUMN_NAME_PERIOD_ID])
    ]
)
data class PeriodicCategoryEntity(
    val estimate: Double?,
    val categoryId: Int,
    val periodId: Int
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
