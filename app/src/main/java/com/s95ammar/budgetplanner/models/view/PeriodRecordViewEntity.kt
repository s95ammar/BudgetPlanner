package com.s95ammar.budgetplanner.models.view

data class PeriodRecordViewEntity(
    val categoryName: String,
    val categoryId: Int,
    var max: Int?,
    val amount: Int,
    val isSelected: Boolean
)