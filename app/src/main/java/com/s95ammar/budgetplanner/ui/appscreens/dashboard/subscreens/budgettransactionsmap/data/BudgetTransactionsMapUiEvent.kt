package com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgettransactionsmap.data

import com.s95ammar.budgetplanner.ui.common.LoadingState

sealed class BudgetTransactionsMapUiEvent {
    class DisplayLoadingState(val loadingState: LoadingState): BudgetTransactionsMapUiEvent()
}
