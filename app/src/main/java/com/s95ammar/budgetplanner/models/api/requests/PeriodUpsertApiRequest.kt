package com.s95ammar.budgetplanner.models.api.requests

import com.google.gson.annotations.SerializedName

sealed class PeriodUpsertApiRequest {

    data class Insertion(
        @SerializedName("name") val name: String?,
        @SerializedName("max") val max: Int?
    ) : PeriodUpsertApiRequest()

    data class Update(
        @SerializedName("id") val id: Int?,
        @SerializedName("name") val name: String?,
        @SerializedName("max") val max: Int?
    ) : PeriodUpsertApiRequest()

}