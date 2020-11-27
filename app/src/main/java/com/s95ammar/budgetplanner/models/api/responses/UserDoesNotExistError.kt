package com.s95ammar.budgetplanner.models.api.responses

class UserDoesNotExistError : ApiError(
    code = IntApiErrorCode.LOGIN_USER_DOES_NOT_EXIST
)