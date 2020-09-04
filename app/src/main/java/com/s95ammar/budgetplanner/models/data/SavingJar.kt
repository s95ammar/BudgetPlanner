package com.s95ammar.budgetplanner.models.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "savings_jar")
data class SavingJar(
    val goal: Long,
    val saved: Long
) {
    @PrimaryKey(autoGenerate = true)
    var id = 0
}