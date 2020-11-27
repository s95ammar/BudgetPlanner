package com.s95ammar.budgetplanner.ui.appscreens.auth.login.validation

import com.s95ammar.budgetplanner.ui.appscreens.auth.common.AuthUtil
import com.s95ammar.budgetplanner.ui.appscreens.auth.login.data.UserLoginInputData
import com.s95ammar.budgetplanner.ui.common.validation.Validator
import com.s95ammar.budgetplanner.ui.common.validation.ViewValidation

class LoginValidator(inputEntity: UserLoginInputData) : Validator<UserLoginInputData, UserLoginInputData>(inputEntity) {

    object Errors {
        const val ERROR_EMAIL_EMPTY = 1
        const val ERROR_EMAIL_INVALID = 2
        const val ERROR_EMAIL_NOT_REGISTERED = 3
        const val ERROR_PASSWORD_EMPTY = 4
        const val ERROR_PASSWORD_LENGTH = 5
        const val ERROR_PASSWORD_INVALID = 6
        const val ERROR_PASSWORD_INCORRECT = 7
    }

    object ViewKeys {
        const val VIEW_EMAIL = 1
        const val VIEW_PASSWORD = 2
    }

    override fun provideOutputEntity(inputEntity: UserLoginInputData): UserLoginInputData {
        return UserLoginInputData(inputEntity.email, inputEntity.password)
    }

    override fun provideViewValidationList(): List<ViewValidation> {
        val caseEmptyEmail = ViewValidation.Case(Errors.ERROR_EMAIL_EMPTY) { inputEntity.email.isEmpty() }

        val caseEmptyInvalid = ViewValidation.Case(Errors.ERROR_EMAIL_INVALID) {
            !AuthUtil.isEmailValid(inputEntity.email)
        }

        val casePasswordEmpty = ViewValidation.Case(Errors.ERROR_PASSWORD_EMPTY) { inputEntity.password.isEmpty() }

        val casePasswordLength = ViewValidation.Case(Errors.ERROR_PASSWORD_LENGTH) {
            inputEntity.password.length < AuthUtil.PASSWORD_LENGTH_8_DIGITS
        }

        val casePasswordInvalid = ViewValidation.Case(Errors.ERROR_PASSWORD_INVALID) {
            !AuthUtil.isPasswordValid(inputEntity.password)
        }

        return listOf(
            ViewValidation(ViewKeys.VIEW_EMAIL, listOf(caseEmptyEmail, caseEmptyInvalid)),
            ViewValidation(ViewKeys.VIEW_PASSWORD, listOf(casePasswordEmpty, casePasswordLength, casePasswordInvalid)),
        )

    }
}