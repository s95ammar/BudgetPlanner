package com.s95ammar.budgetplanner.models.api.responses.errors

import com.s95ammar.budgetplanner.models.api.responses.IntApiErrorCode

class IncorrectPasswordError : ApiError(
    code = IntApiErrorCode.LOGIN_INVALID_PASSWORD
)