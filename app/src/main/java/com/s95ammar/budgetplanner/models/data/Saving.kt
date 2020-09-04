package com.s95ammar.budgetplanner.models.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = CategoryStatus::class,
            parentColumns = ["id"],
            childColumns = ["savings_jar_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Saving(
    val name: String,
    val amount: Int,
    @ColumnInfo(name = "savings_jar_id", index = true) val savingJarId: Int
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}