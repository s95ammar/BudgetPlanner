package com.s95ammar.budgetplanner.models.datasource.local.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.s95ammar.budgetplanner.models.IntBudgetTransactionType
import com.s95ammar.budgetplanner.models.datasource.local.db.BudgetPlannerDbConfig

@Entity(
    tableName = BudgetPlannerDbConfig.TABLE_NAME_BUDGET_TRANSACTION,
    foreignKeys = [
        ForeignKey(
            entity = PeriodicCategoryEntity::class,
            parentColumns = [BudgetPlannerDbConfig.COLUMN_NAME_ID],
            childColumns = [BudgetPlannerDbConfig.COLUMN_NAME_PERIODIC_CATEGORY_ID],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = [BudgetPlannerDbConfig.COLUMN_NAME_PERIODIC_CATEGORY_ID])
    ]
)
data class BudgetTransactionEntity(
    val name: String,
    @IntBudgetTransactionType val type: Int,
    val amount: Int,
    val creationUnixMs: Long,
    val periodicCategoryId: Int
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
