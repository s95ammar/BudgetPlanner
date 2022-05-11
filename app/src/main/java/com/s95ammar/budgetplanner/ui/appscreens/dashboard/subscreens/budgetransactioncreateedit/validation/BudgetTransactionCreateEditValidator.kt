package com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgetransactioncreateedit.validation

import com.s95ammar.budgetplanner.models.datasource.local.db.entity.BudgetTransactionEntity
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.common.data.BudgetTransaction
import com.s95ammar.budgetplanner.ui.common.validation.UnhandledValidationException
import com.s95ammar.budgetplanner.ui.common.validation.Validator
import com.s95ammar.budgetplanner.ui.common.validation.ViewValidation
import com.s95ammar.budgetplanner.util.INVALID

class BudgetTransactionCreateEditValidator(
    private val editedBudgetTransaction: BudgetTransaction?,
    input: BudgetTransactionValidationBundle
) : Validator<BudgetTransactionValidationBundle, BudgetTransactionEntity>(input) {

    object Errors {
        const val EMPTY_NAME = 1
        const val EMPTY_AMOUNT = 2
        const val PERIODIC_CATEGORY_NOT_SELECTED = 3
    }

    object ViewKeys {
        const val VIEW_NAME = 1
        const val VIEW_AMOUNT = 2
        const val VIEW_CATEGORY = 3
    }

    override fun provideOutput(input: BudgetTransactionValidationBundle): BudgetTransactionEntity {
        val amount = input.amount.toIntOrNull() ?: throw UnhandledValidationException(
            "${BudgetTransactionValidationBundle::amount.name} is not a valid Int. Actual value = ${input.amount}"
        )

        return if (editedBudgetTransaction == null) {
            BudgetTransactionEntity(
                name = input.name,
                type = input.type,
                amount = amount,
                periodicCategoryId = input.periodicCategoryId,
                latLng = input.latLng
            )
        } else {
            BudgetTransactionEntity(
                name = input.name,
                type = input.type,
                amount = amount,
                periodicCategoryId = input.periodicCategoryId,
                latLng = input.latLng,
                creationUnixMs = editedBudgetTransaction.creationUnixMs
            ).apply {
                id = editedBudgetTransaction.id
            }
        }
    }

    override fun provideViewValidationList(): List<ViewValidation> {
        val caseEmptyName = ViewValidation.Case(Errors.EMPTY_NAME) { input.name.isEmpty() }
        val caseEmptyAmount = ViewValidation.Case(Errors.EMPTY_AMOUNT) { input.amount.isEmpty() }
        val casePeriodicCategoryNotSelected =
            ViewValidation.Case(Errors.PERIODIC_CATEGORY_NOT_SELECTED) { input.periodicCategoryId == Int.INVALID }

        return listOf(
            ViewValidation(ViewKeys.VIEW_NAME, listOf(caseEmptyName)),
            ViewValidation(ViewKeys.VIEW_AMOUNT, listOf(caseEmptyAmount)),
            ViewValidation(ViewKeys.VIEW_CATEGORY, listOf(casePeriodicCategoryNotSelected))
        )
    }
}