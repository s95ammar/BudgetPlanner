package com.s95ammar.budgetplanner.ui.common.validation

data class ValidationErrors(val viewsErrors: List<ViewErrors>) : Throwable() {

    companion object {
        const val ERROR_NONE = 0
    }

    data class ViewErrors(val viewKey: Int, val errorsIds: List<Int>)
}