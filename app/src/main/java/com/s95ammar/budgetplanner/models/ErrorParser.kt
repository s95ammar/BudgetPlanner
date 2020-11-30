package com.s95ammar.budgetplanner.models

import com.google.gson.Gson
import com.s95ammar.budgetplanner.models.api.responses.*
import com.s95ammar.budgetplanner.models.api.responses.errors.*
import com.s95ammar.budgetplanner.util.orEmptyJson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.net.HttpURLConnection.*

object ErrorParser {

    suspend fun <T> parseApiError(response: Response<T>): ApiError = withContext(Dispatchers.IO) {
        val httpErrorCode = response.code()
        val errorJson = response.errorBody()?.string()?.takeUnless { it.isEmpty() }?.orEmptyJson()

        val errorClassType = when (httpErrorCode) {
            HTTP_BAD_REQUEST-> BadRequestError::class.java
            HTTP_FORBIDDEN -> ForbiddenError::class.java
            HTTP_NOT_FOUND -> NotFoundError::class.java
            HTTP_CONFLICT -> ConflictError::class.java
            HTTP_INTERNAL_ERROR -> InternalServerError::class.java
            HTTP_UNAUTHORIZED -> UnauthorizedError::class.java
            else -> ApiError::class.java
        }

        return@withContext Gson().fromJson(errorJson, errorClassType)
    }

}