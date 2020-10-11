package com.s95ammar.budgetplanner.models

interface ResultStateListener<T> {

    fun onSuccess(data: T? = null)

    fun onLoading()

    fun onError(throwable: Throwable)

}