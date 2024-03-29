package com.s95ammar.budgetplanner.ui.appscreens.categories.subscreens.createedit.validation

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
        const val EMPTY_TITLE = 1
        const val NAME_TAKEN = 2
    }

    object ViewKeys {
        const val VIEW_NAME = 1
    }

    override fun provideOutput(input: CategoryInputBundle): CategoryEntity {
        return CategoryEntity(input.title).apply { if (categoryId != Int.INVALID) id = categoryId }
    }

    override fun provideViewValidationList(input: CategoryInputBundle): List<ViewValidation> {
        val caseEmptyTitle = ViewValidation.Case(Errors.EMPTY_TITLE) { input.title.isEmpty() }

        return listOf(
            ViewValidation(ViewKeys.VIEW_NAME, listOf(caseEmptyTitle))
        )
    }

}