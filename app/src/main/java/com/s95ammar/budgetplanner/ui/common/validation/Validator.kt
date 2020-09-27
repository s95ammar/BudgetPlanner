package com.s95ammar.budgetplanner.ui.common.validation

import com.s95ammar.budgetplanner.ui.common.Constants

abstract class Validator<InputEntity, OutputEntity>(private val inputEntity: InputEntity) {

    private val viewsValidation by lazy { provideViewValidationList() }

    private fun isAllValid(): Boolean {
        return viewsValidation.all { singleViewValidation -> singleViewValidation.validationCases.all { it.isValid } }
    }

    private fun getViewsErrors(): List<ViewErrors> {
        return viewsValidation.map { singleViewValidation ->
            ViewErrors(
                viewKey = singleViewValidation.viewKey,
                errorsIds = singleViewValidation.validationCases.map { it.errorId }
            )
        }
    }

    fun getValidationResult(): ValidationResult<OutputEntity> {
        return if (isAllValid())
            ValidationResult.Success(provideOutputEntity(inputEntity))
        else
            ValidationResult.Error(ValidationErrors(getViewsErrors()))
    }

    protected abstract fun provideOutputEntity(inputEntity: InputEntity): OutputEntity

    protected abstract fun provideViewValidationList(): List<ViewValidation>
}

data class ViewValidation(val viewKey: Int, val validationCases: List<ValidationCase>)

class ValidationCase(errorCaseCallback: () -> Boolean, errorIdIfProduced: Int) {
    val isValid = !errorCaseCallback()
    val errorId = if (isValid) ValidationErrors.ERROR_NONE else errorIdIfProduced
}

data class ValidationErrors(val viewsErrors: List<ViewErrors>) : Throwable() {
    companion object {
        const val ERROR_NONE = Constants.NO_ITEM
    }
}
data class ViewErrors(val viewKey: Int, val errorsIds: List<Int>)

sealed class ValidationResult<OutputEntity> {

    class Success<T>(val outputData: T) : ValidationResult<T>()
    class Error<T>(val throwable: ValidationErrors) : ValidationResult<T>()
}
