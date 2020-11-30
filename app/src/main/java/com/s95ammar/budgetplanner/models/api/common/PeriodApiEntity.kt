package com.s95ammar.budgetplanner.models.api.common

import com.google.gson.annotations.SerializedName

data class PeriodApiEntity(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("max") val max: Int? = null,
    @SerializedName("userId") val userId: Int? = null
)