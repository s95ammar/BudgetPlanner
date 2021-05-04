package com.s95ammar.budgetplanner.ui.appscreens.dashboard.data

import com.s95ammar.budgetplanner.ui.appscreens.dashboard.common.data.PeriodSimple
import com.s95ammar.budgetplanner.ui.common.IntLoadingType
import com.s95ammar.budgetplanner.ui.common.LoadingState

sealed class DashboardUiEvent {
    object NavigateToPeriodsList : DashboardUiEvent()
    object NavigateToCreatePeriod : DashboardUiEvent()
    class NavigateToEditPeriod(val period: PeriodSimple) : DashboardUiEvent()
    class NavigateToCreateBudgetTransaction(val periodId: Int) : DashboardUiEvent()
    class NavigateToEditBudgetTransaction(val periodId: Int, val budgetTransactionId: Int) : DashboardUiEvent()
    class DisplayLoadingState(val loadingState: LoadingState, @IntLoadingType val loadingType: Int): DashboardUiEvent()
}