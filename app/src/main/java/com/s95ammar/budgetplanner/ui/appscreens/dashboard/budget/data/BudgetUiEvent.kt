package com.s95ammar.budgetplanner.ui.appscreens.dashboard.budget.data

import com.s95ammar.budgetplanner.ui.appscreens.auth.common.LoadingState

sealed class BudgetUiEvent {
    class OnNavigateToPeriodRecords(val periodRecordsNavigationBundle: PeriodRecordsNavigationBundle): BudgetUiEvent()
    class DisplayLoadingState(val loadingState: LoadingState): BudgetUiEvent()
}
