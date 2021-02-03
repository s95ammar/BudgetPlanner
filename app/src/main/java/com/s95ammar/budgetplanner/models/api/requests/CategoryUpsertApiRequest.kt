package com.s95ammar.budgetplanner.models.api.requests

sealed class CategoryUpsertApiRequest {

    data class Insertion(
        val name: String?
    ) : CategoryUpsertApiRequest()

    data class Update(
        val id: Int?,
        val name: String?
    ) : CategoryUpsertApiRequest()

}