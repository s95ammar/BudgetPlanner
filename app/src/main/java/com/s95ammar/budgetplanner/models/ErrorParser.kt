package com.s95ammar.budgetplanner.models

import com.google.gson.Gson
import com.s95ammar.budgetplanner.models.api.responses.ApiErrorResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody

object ErrorParser {

    suspend fun parseApiErrorResponse(responseErrorBody: ResponseBody?): ApiErrorResponse = withContext(Dispatchers.IO) {
        val errorJson = responseErrorBody?.string()
        return@withContext Gson().fromJson(errorJson, ApiErrorResponse::class.java)
    }

    suspend fun getApiErrorCode(responseErrorBody: ResponseBody?): Int = withContext(Dispatchers.IO) {
        return@withContext parseApiErrorResponse(responseErrorBody).code
    }
}