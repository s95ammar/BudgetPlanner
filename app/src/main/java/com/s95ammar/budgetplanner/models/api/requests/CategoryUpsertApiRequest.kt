package com.s95ammar.budgetplanner.models.api.requests

import com.google.gson.annotations.SerializedName

sealed class CategoryUpsertApiRequest {

    data class Insertion(
        @SerializedName("name") val name: String?
    ) : CategoryUpsertApiRequest()

    data class Update(
        @SerializedName("id") val id: Int?,
        @SerializedName("name") val name: String?
    ) : CategoryUpsertApiRequest()

}