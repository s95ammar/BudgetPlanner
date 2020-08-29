package com.s95ammar.budgetplanner.models.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "budget_transaction",
    foreignKeys = [
        ForeignKey(
            entity = CategoryStatus::class,
            parentColumns = ["id"],
            childColumns = ["category_status_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class BudgetTransaction(
    val name: String,
    @IntBudgetTransactionType val type: Int,
    val amount: Int,
    @ColumnInfo(name = "category_status_id", index = true) val categoryStatusId: Int
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}