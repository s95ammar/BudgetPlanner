package com.s95ammar.budgetplanner.ui.common.validation

sealed class ValidationResult<OutputEntity> {

    class Success<T>(val outputData: T) : ValidationResult<T>()

    class Error<T>(val throwable: ValidationErrors) : ValidationResult<T>()

    inline fun onSuccess(action: (OutputEntity) -> Unit): ValidationResult<OutputEntity> {
        if (this is Success)
            action(outputData)

        return this
    }

    inline fun onError(action: (ValidationErrors) -> Unit): ValidationResult<OutputEntity> {
        if (this is Error)
            action(throwable)

        return this
    }
}