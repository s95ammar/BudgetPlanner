package com.s95ammar.budgetplanner.models

sealed class Result {

    object Success : Result()

    object Loading : Result()

    class Error(val throwable: Throwable) : Result()

}