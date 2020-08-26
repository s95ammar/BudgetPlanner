package com.s95ammar.budgetplanner.models.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Budget(
    val name: String,
    val initialBalance: Long
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}