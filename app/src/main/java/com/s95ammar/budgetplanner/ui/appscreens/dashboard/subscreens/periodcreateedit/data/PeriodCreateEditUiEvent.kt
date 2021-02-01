package com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.periodcreateedit.data

import com.s95ammar.budgetplanner.ui.appscreens.auth.common.LoadingState
import com.s95ammar.budgetplanner.ui.common.validation.ValidationErrors

sealed class PeriodCreateEditUiEvent {
    class DisplayLoadingState(val loadingState: LoadingState): PeriodCreateEditUiEvent()
    object SetResult: PeriodCreateEditUiEvent()
    class DisplayValidationResults(val validationErrors: ValidationErrors): PeriodCreateEditUiEvent()
    object Exit: PeriodCreateEditUiEvent()
}
