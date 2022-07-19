package com.s95ammar.budgetplanner.ui.common.validation

sealed class ValidationResult<OutputEntity> {

    class Success<T>(val outputData: T) : ValidationResult<T>()

    class Error<T>(val throwable: ValidationErrors) : ValidationResult<T>()

    inline fun handle(
        onSuccess: (OutputEntity) -> Unit,
        onError: (ValidationErrors) -> Unit
    ) {
        when (this) {
            is Error -> onError(throwable)
            is Success -> onSuccess(outputData)
        }
    }
}
