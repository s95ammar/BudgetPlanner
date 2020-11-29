package com.s95ammar.budgetplanner.models.api.responses.errors

import com.google.gson.annotations.SerializedName

open class ApiError(
    @SerializedName("description") val description: String? = null,
    @SerializedName("body") val body: Any? = null
): Exception(description)