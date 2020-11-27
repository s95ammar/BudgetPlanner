package com.s95ammar.budgetplanner.models.api.responses

object UnknownApiError: ApiError(
    code = IntApiErrorCode.UNKNOWN,
    description = "Unknown api error"
)