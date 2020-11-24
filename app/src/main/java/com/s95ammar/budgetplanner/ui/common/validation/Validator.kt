package com.s95ammar.budgetplanner.ui.common.validation

abstract class Validator<InputEntity, OutputEntity>(protected val inputEntity: InputEntity) {

    private val viewsValidation by lazy { provideViewValidationList() }

    private fun isAllValid(): Boolean {
        return viewsValidation.all { singleViewValidation -> singleViewValidation.validationCases.all { it.isValid } }
    }

    private fun getValidationErrors(): ValidationErrors {
        return ValidationErrors(
            viewsValidation.map { singleViewValidation ->
                ValidationErrors.ViewErrors(
                    viewKey = singleViewValidation.viewKey,
                    errorsIds = singleViewValidation.validationCases.map { it.errorId }
                )
            }
        )
    }

    fun getValidationResult(): ValidationResult<OutputEntity> {
        return if (isAllValid())
            ValidationResult.Success(provideOutputEntity(inputEntity))
        else
            ValidationResult.Error(getValidationErrors())
    }

    protected abstract fun provideOutputEntity(inputEntity: InputEntity): OutputEntity

    protected abstract fun provideViewValidationList(): List<ViewValidation>
}