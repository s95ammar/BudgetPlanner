package com.s95ammar.budgetplanner.models.api.responses

class UserAlreadyExistsError : ApiError(
    code = IntApiErrorCode.REGISTER_EMAIL_TAKEN
)