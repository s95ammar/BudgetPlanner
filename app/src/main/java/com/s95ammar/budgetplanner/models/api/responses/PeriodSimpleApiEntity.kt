package com.s95ammar.budgetplanner.models.api.responses

import com.google.gson.annotations.SerializedName

data class PeriodSimpleApiEntity(
    @SerializedName("id") val id: Int?,
    @SerializedName("name") val name: String?,
    @SerializedName("max") val max: Int?,
    @SerializedName("creationUnixMs") val creationUnixMs: Long?,
    @SerializedName("userId") val userId: Int?
)