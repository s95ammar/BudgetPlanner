package com.s95ammar.budgetplanner.models.datasource.remote.api.requests

sealed class CategoryUpsertApiRequest {

    data class Insertion(
        val name: String?
    ) : CategoryUpsertApiRequest()

    data class Update(
        val id: Int?,
        val name: String?
    ) : CategoryUpsertApiRequest()

}