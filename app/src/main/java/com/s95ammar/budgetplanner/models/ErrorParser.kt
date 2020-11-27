package com.s95ammar.budgetplanner.models

import com.google.gson.Gson
import com.s95ammar.budgetplanner.models.api.responses.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

object ErrorParser {

    suspend fun <T> parseApiError(response: Response<T>): ApiError = withContext(Dispatchers.IO) {
        val httpErrorCode = response.code()
        val errorJson = response.errorBody()?.string()
        val generalApiError = Gson().fromJson(errorJson, ApiError::class.java)

        when (httpErrorCode) {
            400 -> {
                when (generalApiError.code) {
                    IntApiErrorCode.REGISTER_EMAIL_TAKEN -> return@withContext UserAlreadyExistsError()
                }
            }
            404 -> {
                when (generalApiError.code) {
                    IntApiErrorCode.LOGIN_USER_DOES_NOT_EXIST -> return@withContext UserDoesNotExistError()
                }
            }
            422 -> {
                when (generalApiError.code) {
                    IntApiErrorCode.LOGIN_INVALID_PASSWORD -> return@withContext IncorrectPasswordError()
                }
            }
        }

        return@withContext UnknownApiError
    }

}