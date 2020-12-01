package com.s95ammar.budgetplanner.ui.appscreens.dashboard.budget.periods.createedit.validation

import com.s95ammar.budgetplanner.models.api.requests.PeriodUpsertApiRequest
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.budget.periods.createedit.data.PeriodInputBundle
import com.s95ammar.budgetplanner.ui.common.validation.Validator
import com.s95ammar.budgetplanner.ui.common.validation.ViewValidation
import com.s95ammar.budgetplanner.util.NO_ITEM

class PeriodCreateEditValidator(
    private val periodId: Int,
    periodInputBundle: PeriodInputBundle
) : Validator<PeriodInputBundle, PeriodUpsertApiRequest>(periodInputBundle) {

    object Errors {
        const val EMPTY_TITLE = 1
        const val NAME_TAKEN = 2
    }

    object ViewKeys {
        const val VIEW_TITLE = 1
    }

        override fun provideOutputEntity(inputEntity: PeriodInputBundle): PeriodUpsertApiRequest {
            return if (periodId == Int.NO_ITEM) PeriodUpsertApiRequest.Insertion(
                name = inputEntity.title,
                max = inputEntity.max?.toIntOrNull()
            ) else PeriodUpsertApiRequest.Update(
                id = periodId,
                name = inputEntity.title,
                max = inputEntity.max?.toIntOrNull()
            )
        }

        override fun provideViewValidationList(): List<ViewValidation> {
            val caseEmptyTitle = ViewValidation.Case(Errors.EMPTY_TITLE) { inputEntity.title.isEmpty() }

            return listOf(
                ViewValidation(ViewKeys.VIEW_TITLE, listOf(caseEmptyTitle))
            )
        }

}