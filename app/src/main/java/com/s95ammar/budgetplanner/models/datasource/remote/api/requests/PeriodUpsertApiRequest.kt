package com.s95ammar.budgetplanner.models.datasource.remote.api.requests

sealed class PeriodUpsertApiRequest {

    data class Insertion(
        val name: String?,
        val max: Int?,
        val periodicCategories: List<PeriodicCategoryUpsertApiRequest>?
    ) : PeriodUpsertApiRequest()

    data class Update(
        val id: Int?,
        val name: String?,
        val max: Int?,
        val periodicCategories: List<PeriodicCategoryUpsertApiRequest>?
    ) : PeriodUpsertApiRequest()

}