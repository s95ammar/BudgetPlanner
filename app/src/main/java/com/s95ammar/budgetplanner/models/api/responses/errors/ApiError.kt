package com.s95ammar.budgetplanner.models.api.responses.errors

import com.google.gson.annotations.SerializedName
import com.s95ammar.budgetplanner.models.api.responses.IntApiErrorCode

open class ApiError(
    @SerializedName("code") @IntApiErrorCode val code: Int = IntApiErrorCode.UNKNOWN,
    @SerializedName("description") val description: String? = null,
    @SerializedName("body") val body: Any? = null
): Exception(description)