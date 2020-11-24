package com.s95ammar.budgetplanner.ui.appscreens.auth.register.validation

import com.s95ammar.budgetplanner.models.api.requests.UserCredentials
import com.s95ammar.budgetplanner.ui.appscreens.auth.common.AuthUtil
import com.s95ammar.budgetplanner.ui.appscreens.auth.register.data.UserRegisterInputData
import com.s95ammar.budgetplanner.ui.common.validation.Validator
import com.s95ammar.budgetplanner.ui.common.validation.ViewValidation

class RegisterValidator(inputEntity: UserRegisterInputData) : Validator<UserRegisterInputData, UserCredentials>(inputEntity) {

    override fun provideOutputEntity(inputEntity: UserRegisterInputData): UserCredentials {
        return UserCredentials(inputEntity.email, inputEntity.password)
    }

    override fun provideViewValidationList(): List<ViewValidation> {
        val caseEmptyEmail = ViewValidation.Case(RegisterValidationErrors.ERROR_EMPTY_EMAIL) { inputEntity.email.isEmpty() }

        val caseEmptyInvalid = ViewValidation.Case(RegisterValidationErrors.ERROR_INVALID_EMAIL) {
            !AuthUtil.isEmailValid(inputEntity.email)
        }

        val casePasswordEmpty = ViewValidation.Case(RegisterValidationErrors.ERROR_EMPTY_PASSWORD) { inputEntity.password.isEmpty() }

        val casePasswordInvalid = ViewValidation.Case(RegisterValidationErrors.ERROR_INVALID_PASSWORD) {
            !AuthUtil.isPasswordValid(inputEntity.password)
        }

        val casePasswordConfirmationEmpty = ViewValidation.Case(RegisterValidationErrors.ERROR_EMPTY_PASSWORD_CONFIRMATION) {
            inputEntity.passwordConfirmation.isEmpty()
        }

        val casePasswordsDoNotMatch = ViewValidation.Case(RegisterValidationErrors.ERROR_PASSWORDS_DO_NOT_MATCH) {
            casePasswordInvalid.isValid && inputEntity.password != inputEntity.passwordConfirmation
        }

        return listOf(
            ViewValidation(RegisterValidationViewKeys.VIEW_EMAIL, listOf(caseEmptyEmail, caseEmptyInvalid)),
            ViewValidation(RegisterValidationViewKeys.VIEW_PASSWORD, listOf(casePasswordEmpty, casePasswordInvalid)),
            ViewValidation(
                RegisterValidationViewKeys.VIEW_PASSWORD_CONFIRMATION, listOf(casePasswordConfirmationEmpty, casePasswordsDoNotMatch)
            )
        )

    }
}