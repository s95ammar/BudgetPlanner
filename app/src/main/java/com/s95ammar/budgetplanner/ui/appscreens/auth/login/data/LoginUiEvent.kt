package com.s95ammar.budgetplanner.ui.appscreens.auth.login.data

import com.s95ammar.budgetplanner.ui.appscreens.auth.common.LoadingState
import com.s95ammar.budgetplanner.ui.common.validation.ValidationErrors

sealed class LoginUiEvent {
    object NavigateToRegister: LoginUiEvent()
    class DisplayValidationResult(val validationErrors: ValidationErrors): LoginUiEvent()
    class DisplayLoadingState(val loadingState: LoadingState) : LoginUiEvent()
    object NavigateToDashboard: LoginUiEvent()
}
