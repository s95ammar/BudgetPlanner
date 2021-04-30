package com.s95ammar.budgetplanner.models.datasource.remote.api.responses

sealed class ApiResult<T> {

    class Success<T>(val data: T?) : ApiResult<T>()

    class Error<T>(val throwable: Throwable) : ApiResult<T>()

    inline fun onSuccess(action: (T?) -> Unit): ApiResult<T> {
        if (this is Success)
            action(data)

        return this
    }

    inline fun onError(action: (Throwable) -> Unit): ApiResult<T> {
        if (this is Error)
            action(throwable)

        return this
    }

}