package com.s95ammar.budgetplanner.ui.appscreens.dashboard.dashboard.data

import com.s95ammar.budgetplanner.ui.appscreens.auth.common.LoadingState

sealed class DashboardUiEvent {
    object NavigateToPeriodsList : DashboardUiEvent()
    object NavigateToCreatePeriod : DashboardUiEvent()
    class DisplayLoadingState(val loadingState: LoadingState): DashboardUiEvent()
}