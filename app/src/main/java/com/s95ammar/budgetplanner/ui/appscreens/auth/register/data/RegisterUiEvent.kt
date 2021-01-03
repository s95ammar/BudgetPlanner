package com.s95ammar.budgetplanner.ui.appscreens.auth.register.data

import com.s95ammar.budgetplanner.ui.appscreens.auth.common.LoadingState
import com.s95ammar.budgetplanner.ui.common.validation.ValidationErrors

sealed class RegisterUiEvent {
    object NavigateToLogin: RegisterUiEvent()
    class DisplayValidationResult(val validationErrors: ValidationErrors): RegisterUiEvent()
    class DisplayLoadingState(val loadingState: LoadingState) : RegisterUiEvent()
    object NavigateToDashboard: RegisterUiEvent()
}
