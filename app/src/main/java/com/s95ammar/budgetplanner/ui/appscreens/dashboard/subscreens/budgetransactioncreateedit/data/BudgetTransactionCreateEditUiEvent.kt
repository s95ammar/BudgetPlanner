package com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgetransactioncreateedit.data

import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgetransactioncreateedit.subscreens.locationselection.data.LocationWithAddress
import com.s95ammar.budgetplanner.ui.common.LoadingState
import com.s95ammar.budgetplanner.ui.common.validation.ValidationErrors

sealed class BudgetTransactionCreateEditUiEvent {
    class ChoosePeriodicCategory(val periodId: Int): BudgetTransactionCreateEditUiEvent()
    class ChooseLocation(val currentLocation: LocationWithAddress?) : BudgetTransactionCreateEditUiEvent()
    class DisplayValidationResults(val validationErrors: ValidationErrors): BudgetTransactionCreateEditUiEvent()
    class DisplayLoadingState(val loadingState: LoadingState): BudgetTransactionCreateEditUiEvent()
    object Exit: BudgetTransactionCreateEditUiEvent()
}
