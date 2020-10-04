package com.s95ammar.budgetplanner.ui.common.validation

sealed class ValidationResult<OutputEntity> {

    class Success<T>(val outputData: T) : ValidationResult<T>()

    class Error<T>(val throwable: ValidationErrors) : ValidationResult<T>()

}