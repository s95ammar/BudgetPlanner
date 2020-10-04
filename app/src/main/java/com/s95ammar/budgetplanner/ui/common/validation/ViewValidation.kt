package com.s95ammar.budgetplanner.ui.common.validation

import androidx.annotation.IntRange

data class ViewValidation(val viewKey: Int, val validationCases: List<Case>) {

    class Case(errorCaseCallback: () -> Boolean, @IntRange(from = 1) errorIdIfProduced: Int) {
        val isValid = !errorCaseCallback()
        val errorId = if (isValid) ValidationErrors.ERROR_NONE else errorIdIfProduced
    }

}