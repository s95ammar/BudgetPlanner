package com.s95ammar.budgetplanner.models.api.responses

import com.google.gson.annotations.SerializedName

data class PeriodRecordApiEntity(
    @SerializedName("categoryName") val categoryName: String?,
    @SerializedName("categoryId") val categoryId: Int?,
    @SerializedName("max") val max: Int?,
    @SerializedName("amount") val amount: Int?,
    @SerializedName("isSelected") val isSelected: Boolean?
)