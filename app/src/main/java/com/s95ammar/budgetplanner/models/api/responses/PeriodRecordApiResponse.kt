package com.s95ammar.budgetplanner.models.api.responses

import com.google.gson.annotations.SerializedName

data class PeriodRecordApiResponse(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("max") val max: Int? = null,
    @SerializedName("amount") val amount: Int? = null,
    @SerializedName("categoryId") val categoryId: Int? = null,
    @SerializedName("periodId") val periodId: Int? = null,
    @SerializedName("userId") val userId: Int? = null
)