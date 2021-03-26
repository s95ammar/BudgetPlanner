package com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgetransactioncreateedit.data

import com.s95ammar.budgetplanner.ui.common.validation.ValidationErrors

sealed class BudgetTransactionCreateEditUiEvent {
    object ChooseCategory: BudgetTransactionCreateEditUiEvent()
    class DisplayValidationResults(val validationErrors: ValidationErrors): BudgetTransactionCreateEditUiEvent()
    object Exit: BudgetTransactionCreateEditUiEvent()
}
