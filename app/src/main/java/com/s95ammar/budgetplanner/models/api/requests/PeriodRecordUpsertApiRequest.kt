package com.s95ammar.budgetplanner.models.api.requests

import com.google.gson.annotations.SerializedName

data class PeriodRecordUpsertApiRequest(
    @SerializedName("categoryId") val categoryId: Int,
    @SerializedName("max") val max: Int?
)