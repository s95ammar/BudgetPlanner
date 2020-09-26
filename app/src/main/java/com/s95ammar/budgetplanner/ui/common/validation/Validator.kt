package com.s95ammar.budgetplanner.ui.common.validation

abstract class Validator<InputEntity, OutputEntity>(private val inputEntity: InputEntity) {

    private val viewValidationList by lazy { provideViewValidationList() }

    private fun isAllValid(): Boolean {
        return viewValidationList.all { viewValidation -> viewValidation.validationCases.all { it.isValid() } }
    }

    private fun getViewsErrors(): List<ViewErrors> {
        
        val viewsErrors = mutableListOf<ViewErrors>()

        for (viewValidation in viewValidationList) {
            val errorsIds = mutableListOf<Int>()
            for (validationCase in viewValidation.validationCases) {
                if (!validationCase.isValid())
                    errorsIds.add(validationCase.errorId)
            }
            if (errorsIds.isNotEmpty())
                viewsErrors.add(ViewErrors(viewValidation.viewKey, errorsIds))
        }

        return viewsErrors
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
data class ValidationCase(val isValid: () -> Boolean, val errorId: Int)

data class ValidationErrors(val viewsErrors: List<ViewErrors>) : Throwable()
data class ViewErrors(val viewKey: Int, val errorsIds: List<Int>)

sealed class ValidationResult<OutputEntity> {

    class Success<T>(val outputData: T) : ValidationResult<T>()
    class Error<T>(val throwable: ValidationErrors) : ValidationResult<T>()
}
