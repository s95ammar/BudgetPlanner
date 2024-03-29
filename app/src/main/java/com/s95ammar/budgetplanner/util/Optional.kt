package com.s95ammar.budgetplanner.util

import androidx.lifecycle.LiveData

data class Optional<T>(val value: T?) {

    companion object {
        fun <T> empty(): Optional<T> {
            return Optional(null)
        }

        fun <T> from(value: T): Optional<T> {
            return Optional(value)
        }
    }

    val isEmpty: Boolean
        get() {
            return value == null
        }

    val isNotEmpty: Boolean
        get() = !isEmpty
}


fun <T> T?.asOptional(): Optional<T> {
    return Optional(this)
}

val <T> LiveData<Optional<T>>.optionalValue
    get() = value?.value
