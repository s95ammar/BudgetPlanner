package com.s95ammar.budgetplanner.models.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Budget(
    val name: String,
    @ColumnInfo(name = "total_balance") val totalBalance: Long
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}