package com.s95ammar.budgetplanner.models.api.requests

import com.google.gson.annotations.SerializedName

sealed class PeriodRecordUpsertApiRequest {

    data class Insertion(
        @SerializedName("category_id") val categoryId: Int,
        @SerializedName("period_id") val periodId: Int,
        @SerializedName("max") val max: Int?
    ) : PeriodRecordUpsertApiRequest()

    data class Update(
        @SerializedName("id") val id: Int,
        @SerializedName("max") val max: Int?
    ) : PeriodRecordUpsertApiRequest()

}