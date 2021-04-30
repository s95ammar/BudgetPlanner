package com.s95ammar.budgetplanner.models.datasource.remote.api.requests

data class PeriodicCategoryUpsertApiRequest(
    val categoryId: Int,
    val max: Int?
)