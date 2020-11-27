package com.s95ammar.budgetplanner.models

import com.s95ammar.budgetplanner.models.api.responses.ApiResult
import retrofit2.Response

object ApiResponseMapper {

    suspend fun <T> Response<T>.mapToApiResult(): ApiResult<T> {
        return if (isSuccessful)
            ApiResult.Success(body())
        else
            ApiResult.Error(ErrorParser.parseApiError(this))
    }
}