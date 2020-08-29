package com.s95ammar.budgetplanner.models.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "category_status",
    foreignKeys = [
        ForeignKey(
            entity = Budget::class,
            parentColumns = ["id"],
            childColumns = ["budget_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Category::class,
            parentColumns = ["id"],
            childColumns = ["category_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class CategoryStatus(
    @ColumnInfo(name = "budget_id", index = true) val budgetId: Int,
    @ColumnInfo(name = "category_id", index = true) val categoryId: Int,
    val estimate: Long,
    val balance: Long
) {
    @PrimaryKey(autoGenerate = true)
    var id = 0
}