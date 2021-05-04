package com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.periodcreateedit.validation

import com.s95ammar.budgetplanner.models.datasource.local.db.entity.PeriodEntity
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.periodcreateedit.data.PeriodInputBundle
import com.s95ammar.budgetplanner.ui.common.validation.Validator
import com.s95ammar.budgetplanner.ui.common.validation.ViewValidation
import com.s95ammar.budgetplanner.util.INVALID

class PeriodCreateEditValidator(
    private val periodId: Int,
//    private val periodicCategoryUpsertApiRequestListProvider: () -> List<PeriodicCategoryUpsertApiRequest>,
    periodInputBundle: PeriodInputBundle
) : Validator<PeriodInputBundle, PeriodEntity>(periodInputBundle) {

    object Errors {
        const val EMPTY_NAME = 1
        const val NAME_TAKEN = 2
    }

    object ViewKeys {
        const val VIEW_NAME = 1
    }

    // TODO: add periodic categories to output
    override fun provideOutput(input: PeriodInputBundle): PeriodEntity {
        return PeriodEntity(input.name, input.max?.toIntOrNull()).apply {
            if (periodId != Int.INVALID) id = periodId
        }
    }

    override fun provideViewValidationList(): List<ViewValidation> {
        val caseEmptyTitle = ViewValidation.Case(Errors.EMPTY_NAME) { input.name.isEmpty() }

        return listOf(
            ViewValidation(ViewKeys.VIEW_NAME, listOf(caseEmptyTitle))
        )
    }

}