package com.s95ammar.budgetplanner.models.datasource.local.db.entity

import androidx.annotation.Nullable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.s95ammar.budgetplanner.models.datasource.local.db.BudgetPlannerDbConfig

@Entity(
    tableName = BudgetPlannerDbConfig.TABLE_NAME_BUDGET_TRANSACTION,
    foreignKeys = [
        ForeignKey(
            entity = PeriodicCategoryEntity::class,
            parentColumns = [BudgetPlannerDbConfig.COLUMN_NAME_ID],
            childColumns = [BudgetPlannerDbConfig.COLUMN_NAME_PERIODIC_CATEGORY_ID],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = CurrencyEntity::class,
            parentColumns = [BudgetPlannerDbConfig.COLUMN_NAME_CODE],
            childColumns = [BudgetPlannerDbConfig.COLUMN_NAME_CURRENCY_CODE],
            onDelete = ForeignKey.SET_DEFAULT
        )
    ],
    indices = [
        Index(value = [BudgetPlannerDbConfig.COLUMN_NAME_PERIODIC_CATEGORY_ID])
    ]
)
data class BudgetTransactionEntity(
    val name: String,
    val amount: Double,
    @ColumnInfo(defaultValue = "") val currencyCode: String,
    val periodicCategoryId: Int,
    @Nullable @Embedded val latLng: LatLngEntity?,
    val creationUnixMs: Long = System.currentTimeMillis()
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
