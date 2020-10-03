package com.s95ammar.budgetplanner.models

sealed class Resource<T> {

    class Success<T>(val data: T) : Resource<T>()

    class Loading<T> : Resource<T>()

    class Error<T>(val throwable: Throwable) : Resource<T>()

}