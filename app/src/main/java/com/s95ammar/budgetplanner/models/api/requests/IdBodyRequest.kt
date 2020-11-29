package com.s95ammar.budgetplanner.models.api.requests

import com.google.gson.annotations.SerializedName

data class IdBodyRequest(
    @SerializedName("id") val id: Int? = null
)