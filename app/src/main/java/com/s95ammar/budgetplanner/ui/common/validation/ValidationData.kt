package com.s95ammar.budgetplanner.ui.common.validation

class ValidationData : HashMap<Int, ValidationErrorType>() {

    fun isAllValid(): Boolean {
        return isEmpty() || values.all { it.errorId == ValidationErrorType.NONE }
    }
}