package com.s95ammar.budgetplanner.models

interface ResultStateListener {

    fun onSuccess()

    fun onLoading()

    fun onError(throwable: Throwable)

}