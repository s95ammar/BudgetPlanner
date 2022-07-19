package com.s95ammar.budgetplanner.ui.common.validation

data class ValidationErrors(val viewsErrors: List<ViewErrors>) : Throwable() {

    companion object {
        const val ERROR_NONE = 0
    }

    fun merge(otherValidationErrors: ValidationErrors?) : ValidationErrors {
        if (otherValidationErrors == null || otherValidationErrors.viewsErrors.isEmpty()) return this
        if (viewsErrors.isEmpty()) return otherValidationErrors

        return ValidationErrors(
            buildList {
                viewsErrors.forEach { viewErrors ->
                    val otherViewErrors = otherValidationErrors.viewsErrors.find { it.viewKey == viewErrors.viewKey }
                    if (otherViewErrors != null) {
                        add(viewErrors.merge(otherViewErrors))
                    } else {
                        add(viewErrors)
                    }
                }

                val nonAddedOtherViewErrors = otherValidationErrors.viewsErrors.filterNot { otherViewErrors ->
                    this.any { addedViewErrors -> otherViewErrors.viewKey == addedViewErrors.viewKey }
                }
                nonAddedOtherViewErrors.forEach { otherViewErrors ->
                    add(otherViewErrors)
                }
            }
        )
    }

    data class ViewErrors(val viewKey: Int, val errorsIds: List<Int>) {

        val highestPriorityOrNone: Int
            get() = errorsIds.filterNot { it == ERROR_NONE }.minOrNull() ?: ERROR_NONE

        fun merge(otherViewErrors: ViewErrors): ViewErrors {
            if (otherViewErrors.viewKey != viewKey) return otherViewErrors

            val errorIdsUnion = errorsIds.union(otherViewErrors.errorsIds)
            val errorIds = errorIdsUnion.let { union ->
                if (union.size > 1)
                    union.filterNot { it == ERROR_NONE }
                else
                    union
            }.toList()

            return ViewErrors(viewKey, errorIds)
        }
    }
}