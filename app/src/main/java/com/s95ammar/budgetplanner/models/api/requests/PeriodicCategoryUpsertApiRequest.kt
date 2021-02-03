package com.s95ammar.budgetplanner.models.api.requests

data class PeriodicCategoryUpsertApiRequest(
    val categoryId: Int,
    val max: Int?
)