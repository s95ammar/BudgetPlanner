package com.s95ammar.budgetplanner.models.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BudgetTransaction(
    val name: String,
    @ColumnInfo(name = "budget_id", index = true) val budgetId: Int,
    @ColumnInfo(name = "category_id", index = true) val categoryId: Int
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}