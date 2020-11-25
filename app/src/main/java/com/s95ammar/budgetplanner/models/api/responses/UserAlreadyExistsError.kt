package com.s95ammar.budgetplanner.models.api.responses

class UserAlreadyExistsError : ApiErrorResponse(
    code = IntErrorCode.REGISTER_EMAIL_TAKEN
)