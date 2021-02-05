package com.s95ammar.budgetplanner.models.api

import com.google.gson.Gson
import com.s95ammar.budgetplanner.models.api.responses.errors.*
import com.s95ammar.budgetplanner.util.orEmptyJson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.Response
import java.net.HttpURLConnection

fun <T> Flow<Response<T>>.parseResponse() = map { response ->
    if (response.isSuccessful) {
        response.body() ?: throw EmptyResponseError()
    } else {
        throw parseApiError(response)
    }
}

private fun <T> parseApiError(response: Response<T>): ApiError {
    val httpErrorCode = response.code()
    val errorJson = response.errorBody()?.string()?.takeUnless { it.isEmpty() }?.orEmptyJson()

    val errorClassType = when (httpErrorCode) {
        HttpURLConnection.HTTP_BAD_REQUEST -> BadRequestError::class.java
        HttpURLConnection.HTTP_FORBIDDEN -> ForbiddenError::class.java
        HttpURLConnection.HTTP_NOT_FOUND -> NotFoundError::class.java
        HttpURLConnection.HTTP_CONFLICT -> ConflictError::class.java
        HttpURLConnection.HTTP_INTERNAL_ERROR -> InternalServerError::class.java
        HttpURLConnection.HTTP_UNAUTHORIZED -> UnauthorizedError::class.java
        else -> ApiError::class.java
    }

    return Gson().fromJson(errorJson, errorClassType)
}