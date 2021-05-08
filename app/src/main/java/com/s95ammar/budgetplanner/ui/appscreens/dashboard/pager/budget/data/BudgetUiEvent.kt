package com.s95ammar.budgetplanner.ui.appscreens.dashboard.pager.budget.data

import com.s95ammar.budgetplanner.ui.common.LoadingState

sealed class BudgetUiEvent {
    class DisplayLoadingState(val loadingState: LoadingState): BudgetUiEvent()
}
