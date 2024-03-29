package com.s95ammar.budgetplanner.ui.appscreens.dashboard.data

import com.s95ammar.budgetplanner.ui.appscreens.dashboard.common.data.CategoryOfPeriod
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.common.data.PeriodSimple
import com.s95ammar.budgetplanner.ui.common.LoadingState

sealed class DashboardUiEvent {
    object NavigateToPeriodsList : DashboardUiEvent()
    object NavigateToCreatePeriod : DashboardUiEvent()
    class NavigateToEditPeriod(val period: PeriodSimple) : DashboardUiEvent()
    class NavigateToCreateEditEstimate(val categoryOfPeriod: CategoryOfPeriod) : DashboardUiEvent()
    class NavigateToCreateBudgetTransaction(val periodId: Int) : DashboardUiEvent()
    class NavigateToEditBudgetTransaction(val periodId: Int, val budgetTransactionId: Int) : DashboardUiEvent()
    class DisplayLoadingState(val loadingState: LoadingState): DashboardUiEvent()
    class NavigateToBudgetTransactionsMap(val periodId: Int) : DashboardUiEvent()
}