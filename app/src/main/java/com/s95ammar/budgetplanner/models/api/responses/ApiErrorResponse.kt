package com.s95ammar.budgetplanner.models.api.responses

import com.google.gson.annotations.SerializedName

open class ApiErrorResponse(
    @SerializedName("code") @IntErrorCode val code: Int = IntErrorCode.UNKNOWN,
    @SerializedName("description") val description: String? = null,
    @SerializedName("body") val body: Any? = null
): Exception(description)