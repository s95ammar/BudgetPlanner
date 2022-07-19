package com.s95ammar.budgetplanner.ui.appscreens.categories.subscreens.createedit.validation

import com.s95ammar.budgetplanner.R
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.CategoryEntity
import com.s95ammar.budgetplanner.ui.appscreens.categories.subscreens.createedit.data.CategoryInputBundle
import com.s95ammar.budgetplanner.ui.common.validation.Validator
import com.s95ammar.budgetplanner.ui.common.validation.ViewValidation
import com.s95ammar.budgetplanner.util.INVALID

class CategoryCreateEditValidator(
    private val categoryId: Int,
    input: CategoryInputBundle
) : Validator<CategoryInputBundle, CategoryEntity>(input) {

    object Errors {
        const val EMPTY_TITLE = R.string.error_empty_field
        const val NAME_TAKEN = R.string.error_category_name_taken
    }

    object ViewKeys {
        const val VIEW_NAME = 1
    }

    override fun provideOutput(input: CategoryInputBundle): CategoryEntity {
        return CategoryEntity(input.name).apply { if (categoryId != Int.INVALID) id = categoryId }
    }

    override fun provideViewValidationList(input: CategoryInputBundle): List<ViewValidation> {
        val caseEmptyTitle = ViewValidation.Case(Errors.EMPTY_TITLE) { input.name.isEmpty() }

        return listOf(
            ViewValidation(ViewKeys.VIEW_NAME, listOf(caseEmptyTitle))
        )
    }

}