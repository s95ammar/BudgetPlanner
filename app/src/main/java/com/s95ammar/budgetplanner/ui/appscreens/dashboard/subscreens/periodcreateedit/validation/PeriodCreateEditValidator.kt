package com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.periodcreateedit.validation

import com.s95ammar.budgetplanner.models.api.requests.PeriodUpsertApiRequest
import com.s95ammar.budgetplanner.models.api.requests.PeriodicCategoryUpsertApiRequest
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.periodcreateedit.data.PeriodInputBundle
import com.s95ammar.budgetplanner.ui.common.validation.Validator
import com.s95ammar.budgetplanner.ui.common.validation.ViewValidation
import com.s95ammar.budgetplanner.util.NO_ITEM

class PeriodCreateEditValidator(
    private val periodId: Int,
    private val periodicCategoryUpsertApiRequestListProvider: () -> List<PeriodicCategoryUpsertApiRequest>,
    periodInputBundle: PeriodInputBundle
) : Validator<PeriodInputBundle, PeriodUpsertApiRequest>(periodInputBundle) {

    object Errors {
        const val EMPTY_NAME = 1
    }

    object ViewKeys {
        const val VIEW_NAME = 1
    }

    override fun provideOutputEntity(inputEntity: PeriodInputBundle): PeriodUpsertApiRequest {
        return if (periodId == Int.NO_ITEM) PeriodUpsertApiRequest.Insertion(
            name = inputEntity.name,
            max = inputEntity.max?.toIntOrNull(),
            periodicCategories = periodicCategoryUpsertApiRequestListProvider()
        ) else PeriodUpsertApiRequest.Update(
            id = periodId,
            name = inputEntity.name,
            max = inputEntity.max?.toIntOrNull(),
            periodicCategories = periodicCategoryUpsertApiRequestListProvider()
        )
    }

    override fun provideViewValidationList(): List<ViewValidation> {
        val caseEmptyTitle = ViewValidation.Case(Errors.EMPTY_NAME) { inputEntity.name.isEmpty() }

        return listOf(
            ViewValidation(ViewKeys.VIEW_NAME, listOf(caseEmptyTitle))
        )
    }

}