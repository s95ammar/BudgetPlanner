package com.s95ammar.budgetplanner.models

interface ResultStateListener<T> {

    fun onLoading()

    fun onSuccess(data: T? = null)

    fun onError(throwable: Throwable)

}