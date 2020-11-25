package com.s95ammar.budgetplanner.models.api.responses

object UnknownApiError: ApiErrorResponse(
    code = IntErrorCode.UNKNOWN,
    description = "Unknown server error"
)