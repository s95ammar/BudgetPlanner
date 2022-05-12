package com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgetransactioncreateedit.data

import com.google.android.gms.maps.model.LatLng
import com.s95ammar.budgetplanner.ui.common.LoadingState
import com.s95ammar.budgetplanner.ui.common.validation.ValidationErrors

sealed class BudgetTransactionCreateEditUiEvent {
    class ChoosePeriodicCategory(val periodId: Int): BudgetTransactionCreateEditUiEvent()
    class ChooseLocation(val currentLatLng: LatLng?) : BudgetTransactionCreateEditUiEvent()
    class DisplayValidationResults(val validationErrors: ValidationErrors): BudgetTransactionCreateEditUiEvent()
    class DisplayLoadingState(val loadingState: LoadingState): BudgetTransactionCreateEditUiEvent()
    object Exit: BudgetTransactionCreateEditUiEvent()
}
