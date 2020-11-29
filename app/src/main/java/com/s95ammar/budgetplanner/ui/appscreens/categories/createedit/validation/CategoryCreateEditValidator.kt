package com.s95ammar.budgetplanner.ui.appscreens.categories.createedit.validation

import com.s95ammar.budgetplanner.models.api.common.CategoryApiEntity
import com.s95ammar.budgetplanner.ui.appscreens.categories.createedit.data.CategoryInputBundle
import com.s95ammar.budgetplanner.ui.common.validation.Validator
import com.s95ammar.budgetplanner.ui.common.validation.ViewValidation
import com.s95ammar.budgetplanner.util.NO_ITEM

class CategoryCreateEditValidator(
    private val categoryId: Int,
    categoryInputBundle: CategoryInputBundle
) : Validator<CategoryInputBundle, CategoryApiEntity>(categoryInputBundle) {

    object Errors {
        const val EMPTY_TITLE = 1
        const val NAME_TAKEN = 2
    }

    object ViewKeys {
        const val VIEW_TITLE = 1
    }

        override fun provideOutputEntity(inputEntity: CategoryInputBundle): CategoryApiEntity {
            return CategoryApiEntity(
                id = if (categoryId == Int.NO_ITEM) null else categoryId,
                name = inputEntity.title
            )
        }

        override fun provideViewValidationList(): List<ViewValidation> {
            val caseEmptyTitle = ViewValidation.Case(Errors.EMPTY_TITLE) { inputEntity.title.isEmpty() }

            return listOf(
                ViewValidation(ViewKeys.VIEW_TITLE, listOf(caseEmptyTitle))
            )
        }

}