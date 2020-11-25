package com.s95ammar.budgetplanner.ui.appscreens.auth.register.validation

import com.s95ammar.budgetplanner.models.api.requests.UserCredentials
import com.s95ammar.budgetplanner.ui.appscreens.auth.common.AuthUtil
import com.s95ammar.budgetplanner.ui.appscreens.auth.register.data.UserRegisterInputData
import com.s95ammar.budgetplanner.ui.common.validation.Validator
import com.s95ammar.budgetplanner.ui.common.validation.ViewValidation

class RegisterValidator(inputEntity: UserRegisterInputData) : Validator<UserRegisterInputData, UserCredentials>(inputEntity) {

    object Errors {
        const val ERROR_EMPTY_EMAIL = 1
        const val ERROR_INVALID_EMAIL = 2
        const val ERROR_EMPTY_PASSWORD = 3
        const val ERROR_INVALID_PASSWORD = 4
        const val ERROR_EMPTY_PASSWORD_CONFIRMATION = 5
        const val ERROR_PASSWORDS_DO_NOT_MATCH = 6
    }

    object ViewKeys {
        const val VIEW_EMAIL = 1
        const val VIEW_PASSWORD = 2
        const val VIEW_PASSWORD_CONFIRMATION = 3
    }

    override fun provideOutputEntity(inputEntity: UserRegisterInputData): UserCredentials {
        return UserCredentials(inputEntity.email, inputEntity.password)
    }

    override fun provideViewValidationList(): List<ViewValidation> {
        val caseEmptyEmail = ViewValidation.Case(Errors.ERROR_EMPTY_EMAIL) { inputEntity.email.isEmpty() }

        val caseEmptyInvalid = ViewValidation.Case(Errors.ERROR_INVALID_EMAIL) {
            !AuthUtil.isEmailValid(inputEntity.email)
        }

        val casePasswordEmpty = ViewValidation.Case(Errors.ERROR_EMPTY_PASSWORD) { inputEntity.password.isEmpty() }

        val casePasswordInvalid = ViewValidation.Case(Errors.ERROR_INVALID_PASSWORD) {
            !AuthUtil.isPasswordValid(inputEntity.password)
        }

        val casePasswordConfirmationEmpty = ViewValidation.Case(Errors.ERROR_EMPTY_PASSWORD_CONFIRMATION) {
            inputEntity.passwordConfirmation.isEmpty()
        }

        val casePasswordsDoNotMatch = ViewValidation.Case(Errors.ERROR_PASSWORDS_DO_NOT_MATCH) {
            casePasswordInvalid.isValid && inputEntity.password != inputEntity.passwordConfirmation
        }

        return listOf(
            ViewValidation(ViewKeys.VIEW_EMAIL, listOf(caseEmptyEmail, caseEmptyInvalid)),
            ViewValidation(ViewKeys.VIEW_PASSWORD, listOf(casePasswordEmpty, casePasswordInvalid)),
            ViewValidation(
                ViewKeys.VIEW_PASSWORD_CONFIRMATION, listOf(casePasswordConfirmationEmpty, casePasswordsDoNotMatch)
            )
        )

    }
}