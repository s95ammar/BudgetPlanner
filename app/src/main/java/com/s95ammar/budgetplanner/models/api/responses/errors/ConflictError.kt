package com.s95ammar.budgetplanner.models.api.responses.errors

class ConflictError(description: String? = null, body: Any? = null) : ApiError(description, body)