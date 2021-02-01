package com.s95ammar.budgetplanner.ui.appscreens.dashboard.data

import com.s95ammar.budgetplanner.ui.appscreens.auth.common.LoadingState
import com.s95ammar.budgetplanner.ui.common.IntLoadingType

sealed class DashboardUiEvent {
    object NavigateToPeriodsList : DashboardUiEvent()
    object NavigateToCreatePeriod : DashboardUiEvent()
    class DisplayLoadingState(val loadingState: LoadingState, @IntLoadingType val loadingType: Int): DashboardUiEvent()
}