package com.s95ammar.budgetplanner.models.datasource.remote.api.responses.errors

open class ApiError(
    val description: String? = null,
    val body: Any? = null
): Exception(description)