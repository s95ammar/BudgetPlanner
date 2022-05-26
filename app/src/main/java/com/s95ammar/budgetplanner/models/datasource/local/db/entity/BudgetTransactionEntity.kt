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
            entity = CategoryOfPeriodEntity::class,
            parentColumns = [BudgetPlannerDbConfig.COLUMN_NAME_ID],
            childColumns = [BudgetPlannerDbConfig.COLUMN_NAME_CATEGORY_OF_PERIOD_ID],
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
        Index(value = [BudgetPlannerDbConfig.COLUMN_NAME_CATEGORY_OF_PERIOD_ID]),
        Index(value = [BudgetPlannerDbConfig.COLUMN_NAME_CURRENCY_CODE])
    ]
)
data class BudgetTransactionEntity(
    val name: String,
    val amount: Double,
    @ColumnInfo(defaultValue = "") val currencyCode: String,
    val categoryOfPeriodId: Int,
    @Nullable @Embedded val latLng: LatLngEntity?,
    val creationUnixMs: Long = System.currentTimeMillis()
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
