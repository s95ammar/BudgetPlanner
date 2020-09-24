package com.s95ammar.budgetplanner.ui.common.validation

open class ValidationErrorType(val errorId: Int) {

    companion object {
        const val NONE = 0
        fun none() = ValidationErrorType(NONE)
    }
}