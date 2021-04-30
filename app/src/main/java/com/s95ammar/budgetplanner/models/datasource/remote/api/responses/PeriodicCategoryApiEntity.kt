package com.s95ammar.budgetplanner.models.datasource.remote.api.responses

data class PeriodicCategoryApiEntity(
    val categoryName: String?,
    val categoryId: Int?,
    val max: Int?,
    val amount: Int?,
    val isSelected: Boolean?
)