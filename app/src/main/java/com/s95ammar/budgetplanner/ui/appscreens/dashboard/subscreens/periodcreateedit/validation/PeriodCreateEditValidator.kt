package com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.periodcreateedit.validation

import com.s95ammar.budgetplanner.models.datasource.local.db.entity.PeriodEntity
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.periodcreateedit.data.PeriodInputBundle
import com.s95ammar.budgetplanner.ui.common.validation.Validator
import com.s95ammar.budgetplanner.ui.common.validation.ViewValidation

class PeriodCreateEditValidator(
    private val periodId: Int,
//    private val periodicCategoryUpsertApiRequestListProvider: () -> List<PeriodicCategoryUpsertApiRequest>,
    periodInputBundle: PeriodInputBundle
) : Validator<PeriodInputBundle, PeriodEntity>(periodInputBundle) {

    object Errors {
        const val EMPTY_NAME = 1
    }

    object ViewKeys {
        const val VIEW_NAME = 1
    }

    override fun provideOutputEntity(inputEntity: PeriodInputBundle): PeriodEntity {
        TODO()
    }

    override fun provideViewValidationList(): List<ViewValidation> {
        val caseEmptyTitle = ViewValidation.Case(Errors.EMPTY_NAME) { inputEntity.name.isEmpty() }

        return listOf(
            ViewValidation(ViewKeys.VIEW_NAME, listOf(caseEmptyTitle))
        )
    }

}