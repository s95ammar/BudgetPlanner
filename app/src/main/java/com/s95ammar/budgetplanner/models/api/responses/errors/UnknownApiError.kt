package com.s95ammar.budgetplanner.models.api.responses.errors

import com.s95ammar.budgetplanner.models.api.responses.IntApiErrorCode

object UnknownApiError: ApiError(
    code = IntApiErrorCode.UNKNOWN,
    description = "Unknown api error"
)