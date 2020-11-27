package com.s95ammar.budgetplanner.models.api.responses

import com.google.gson.annotations.SerializedName

open class ApiError(
    @SerializedName("code") @IntApiErrorCode val code: Int = IntApiErrorCode.UNKNOWN,
    @SerializedName("description") val description: String? = null,
    @SerializedName("body") val body: Any? = null
): Exception(description)