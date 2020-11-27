package com.s95ammar.budgetplanner.models

import com.google.gson.Gson
import com.s95ammar.budgetplanner.models.api.responses.ApiError
import com.s95ammar.budgetplanner.models.api.responses.IntApiErrorCode
import com.s95ammar.budgetplanner.models.api.responses.UnknownApiError
import com.s95ammar.budgetplanner.models.api.responses.UserAlreadyExistsError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Response

object ErrorParser {

    suspend fun <T> parseApiError(response: Response<T>): ApiError = withContext(Dispatchers.IO) {
        val httpErrorCode = response.code()
        val errorJson = response.errorBody()?.string()
        val generalApiError = Gson().fromJson(errorJson, ApiError::class.java)

        when (httpErrorCode) {
            400 -> {
                when (generalApiError.code) {
                    IntApiErrorCode.REGISTER_EMAIL_TAKEN -> { return@withContext UserAlreadyExistsError() }
                }
            }
        }

        return@withContext UnknownApiError
    }

}