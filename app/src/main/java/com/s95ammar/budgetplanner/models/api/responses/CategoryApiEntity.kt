package com.s95ammar.budgetplanner.models.api.responses

import com.google.gson.annotations.SerializedName

data class CategoryApiEntity(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("userId") val userId: Int? = null
)