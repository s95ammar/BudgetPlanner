package com.s95ammar.budgetplanner.models

import com.s95ammar.budgetplanner.models.api.responses.IntErrorCode
import com.s95ammar.budgetplanner.models.api.responses.ApiResult
import com.s95ammar.budgetplanner.models.api.responses.UnknownApiError
import com.s95ammar.budgetplanner.models.api.responses.UserAlreadyExistsError
import retrofit2.Response

object ApiResponseHandler {

    suspend fun <T> Response<T>.mapToApiResult(): ApiResult<T> {
        if (isSuccessful) {
            return ApiResult.Success(body())
        } else {
            // TODO: move to ErrorParser
            val httpErrorCode = code()
            val errorResponse = ErrorParser.parseApiErrorResponse(errorBody())
            when (httpErrorCode) {
                400 -> {
                    when (errorResponse.code) {
                        IntErrorCode.REGISTER_EMAIL_TAKEN -> { return ApiResult.Error(UserAlreadyExistsError()) }
                    }
                }
            }
            return ApiResult.Error(UnknownApiError)
        }
    }
}