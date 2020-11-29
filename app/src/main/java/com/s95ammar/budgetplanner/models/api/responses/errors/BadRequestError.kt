package com.s95ammar.budgetplanner.models.api.responses.errors

class BadRequestError(description: String? = null, body: Any? = null) : ApiError(description, body)