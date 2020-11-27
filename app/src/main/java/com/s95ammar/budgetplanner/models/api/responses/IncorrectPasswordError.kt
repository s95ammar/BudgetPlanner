package com.s95ammar.budgetplanner.models.api.responses

class IncorrectPasswordError : ApiError(
    code = IntApiErrorCode.LOGIN_INVALID_PASSWORD
)