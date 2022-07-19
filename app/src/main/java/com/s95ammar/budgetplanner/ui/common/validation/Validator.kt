package com.s95ammar.budgetplanner.ui.common.validation

abstract class Validator<Input, Output>(private val input: Input) {

    private val viewsValidation by lazy { provideViewValidationList(input) }

    private fun isAllValid(): Boolean {
        return viewsValidation.all { singleViewValidation -> singleViewValidation.validationCases.all { it.isValid } }
    }

    fun getBlankValidationErrors() = getValidationErrors(allBlank = true)

    private fun getValidationErrors(allBlank: Boolean = false): ValidationErrors {
        return ValidationErrors(
            viewsValidation.map { singleViewValidation ->
                ValidationErrors.ViewErrors(
                    viewKey = singleViewValidation.viewKey,
                    errorsIds = singleViewValidation.validationCases.map { if (allBlank) ValidationErrors.ERROR_NONE else it.errorId }
                )
            }
        )
    }

    fun handleValidationResult(
        onSuccess: (Output) -> Unit,
        onError: (ValidationErrors) -> Unit
    ) {
        if (isAllValid())
            onSuccess(provideOutput(input))
        else
            onError(getValidationErrors())
    }

    fun getValidationResult(): ValidationResult<Output> {
        return if (isAllValid())
            ValidationResult.Success(provideOutput(input))
        else
            ValidationResult.Error(getValidationErrors())
    }

    protected abstract fun provideOutput(input: Input): Output

    protected abstract fun provideViewValidationList(input: Input): List<ViewValidation>
}