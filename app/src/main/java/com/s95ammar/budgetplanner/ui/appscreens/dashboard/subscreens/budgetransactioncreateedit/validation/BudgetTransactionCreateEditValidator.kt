package com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgetransactioncreateedit.validation

import com.s95ammar.budgetplanner.models.IntBudgetTransactionType
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.BudgetTransactionEntity
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.LatLngEntity
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.common.data.BudgetTransaction
import com.s95ammar.budgetplanner.ui.common.validation.UnhandledValidationException
import com.s95ammar.budgetplanner.ui.common.validation.Validator
import com.s95ammar.budgetplanner.ui.common.validation.ViewValidation
import com.s95ammar.budgetplanner.util.INVALID
import com.s95ammar.budgetplanner.util.roundToTwoDecimals
import com.s95ammar.budgetplanner.util.toDoubleOrNull
import java.util.Locale

class BudgetTransactionCreateEditValidator(
    private val editedBudgetTransaction: BudgetTransaction?,
    private val locale: Locale,
    input: BudgetTransactionValidationBundle
) : Validator<BudgetTransactionValidationBundle, BudgetTransactionEntity>(input) {

    object Errors {
        const val EMPTY_NAME = 1
        const val EMPTY_AMOUNT = 2
        const val CATEGORY_OF_PERIOD_NOT_SELECTED = 3
    }

    object ViewKeys {
        const val VIEW_NAME = 1
        const val VIEW_AMOUNT = 2
        const val VIEW_CATEGORY = 3
    }

    override fun provideOutput(input: BudgetTransactionValidationBundle): BudgetTransactionEntity {
        val amount = getActualAmountFromInput(input)
        val currencyCode = input.currencyCode ?: throw UnhandledValidationException(
            "${BudgetTransactionValidationBundle::currencyCode.name} cannot be null"
        )

        return if (editedBudgetTransaction == null) {
            BudgetTransactionEntity(
                name = input.name,
                amount = amount,
                currencyCode = currencyCode,
                categoryOfPeriodId = input.categoryOfPeriodId,
                latLng = LatLngEntity.EntityMapper.toEntity(input.latLng)
            )
        } else {
            BudgetTransactionEntity(
                name = input.name,
                amount = amount,
                currencyCode = currencyCode,
                categoryOfPeriodId = input.categoryOfPeriodId,
                latLng = LatLngEntity.EntityMapper.toEntity(input.latLng),
                creationUnixMs = editedBudgetTransaction.creationUnixMs
            ).apply {
                id = editedBudgetTransaction.id
            }
        }
    }

    override fun provideViewValidationList(input: BudgetTransactionValidationBundle): List<ViewValidation> {
        val caseEmptyName = ViewValidation.Case(Errors.EMPTY_NAME) { input.name.isEmpty() }
        val caseEmptyAmount = ViewValidation.Case(Errors.EMPTY_AMOUNT) { input.amount.isEmpty() }
        val caseCategoryOfPeriodNotSelected =
            ViewValidation.Case(Errors.CATEGORY_OF_PERIOD_NOT_SELECTED) { input.categoryOfPeriodId == Int.INVALID }

        return listOf(
            ViewValidation(ViewKeys.VIEW_NAME, listOf(caseEmptyName)),
            ViewValidation(ViewKeys.VIEW_AMOUNT, listOf(caseEmptyAmount)),
            ViewValidation(ViewKeys.VIEW_CATEGORY, listOf(caseCategoryOfPeriodNotSelected))
        )
    }

    private fun getActualAmountFromInput(input: BudgetTransactionValidationBundle): Double {
        return input.amount
            .toDoubleOrNull(locale)
            ?.let { if (input.type == IntBudgetTransactionType.EXPENSE) -it else it }
            ?.roundToTwoDecimals()
            ?: throw UnhandledValidationException(
                "${BudgetTransactionValidationBundle::amount.name} is not a valid Double value. Actual value = ${input.amount}"
            )
    }
}