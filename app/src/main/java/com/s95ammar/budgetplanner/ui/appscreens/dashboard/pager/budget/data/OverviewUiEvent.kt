package com.s95ammar.budgetplanner.ui.appscreens.dashboard.pager.budget.data

import com.s95ammar.budgetplanner.ui.common.LoadingState

sealed class OverviewUiEvent {
    class DisplayLoadingState(val loadingState: LoadingState): OverviewUiEvent()
}
