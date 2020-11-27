package com.s95ammar.budgetplanner.models.api.responses.errors

import com.s95ammar.budgetplanner.models.api.responses.IntApiErrorCode

class UserAlreadyExistsError : ApiError(
    code = IntApiErrorCode.REGISTER_EMAIL_TAKEN
)