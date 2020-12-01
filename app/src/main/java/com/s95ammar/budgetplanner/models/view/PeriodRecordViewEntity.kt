package com.s95ammar.budgetplanner.models.view

data class PeriodRecordViewEntity(
    val id: Int,
    val max: Int?,
    val amount: Int,
    val categoryId: Int,
    val categoryName: String,
    val periodId: Int
)