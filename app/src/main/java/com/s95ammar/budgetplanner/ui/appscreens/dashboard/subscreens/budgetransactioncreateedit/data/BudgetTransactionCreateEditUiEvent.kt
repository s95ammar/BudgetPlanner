package com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgetransactioncreateedit.data

import com.s95ammar.budgetplanner.ui.common.validation.ValidationErrors

sealed class BudgetTransactionCreateEditUiEvent {
    class ChooseCategory(val periodId: Int): BudgetTransactionCreateEditUiEvent()
    class DisplayValidationResults(val validationErrors: ValidationErrors): BudgetTransactionCreateEditUiEvent()
    object Exit: BudgetTransactionCreateEditUiEvent()
}
