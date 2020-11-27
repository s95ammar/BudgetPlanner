package com.s95ammar.budgetplanner.models.api.responses.errors

import com.s95ammar.budgetplanner.models.api.responses.IntApiErrorCode

class UserDoesNotExistError : ApiError(
    code = IntApiErrorCode.LOGIN_USER_DOES_NOT_EXIST
)