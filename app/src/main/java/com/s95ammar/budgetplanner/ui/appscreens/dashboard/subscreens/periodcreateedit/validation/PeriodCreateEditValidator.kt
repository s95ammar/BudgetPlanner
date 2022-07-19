package com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.periodcreateedit.validation

import com.s95ammar.budgetplanner.R
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.PeriodEntity
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.periodcreateedit.data.PeriodInputBundle
import com.s95ammar.budgetplanner.ui.common.validation.Validator
import com.s95ammar.budgetplanner.ui.common.validation.ViewValidation
import com.s95ammar.budgetplanner.util.INVALID

class PeriodCreateEditValidator(
    private val periodId: Int,
    periodInputBundle: PeriodInputBundle
) : Validator<PeriodInputBundle, PeriodEntity>(periodInputBundle) {

    object Errors {
        const val EMPTY_NAME = R.string.error_period_name_taken
        const val NAME_TAKEN = R.string.error_empty_field
    }

    object ViewKeys {
        const val VIEW_NAME = 1
    }

    override fun provideOutput(input: PeriodInputBundle): PeriodEntity {
        return PeriodEntity(input.name).apply {
            if (periodId != Int.INVALID) id = periodId
        }
    }

    override fun provideViewValidationList(input: PeriodInputBundle): List<ViewValidation> {
        val caseEmptyTitle = ViewValidation.Case(Errors.EMPTY_NAME) { input.name.isEmpty() }

        return listOf(
            ViewValidation(ViewKeys.VIEW_NAME, listOf(caseEmptyTitle))
        )
    }

}