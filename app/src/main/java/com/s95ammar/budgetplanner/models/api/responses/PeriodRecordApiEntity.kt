package com.s95ammar.budgetplanner.models.api.responses

import com.google.gson.annotations.SerializedName

data class PeriodRecordApiEntity(
    @SerializedName("id") val id: Int?,
    @SerializedName("max") val max: Int?,
    @SerializedName("amount") val amount: Int?,
    @SerializedName("categoryId") val categoryId: Int?,
    @SerializedName("categoryName") val categoryName: String?,
    @SerializedName("periodId") val periodId: Int?,
    @SerializedName("userId") val userId: Int?
)