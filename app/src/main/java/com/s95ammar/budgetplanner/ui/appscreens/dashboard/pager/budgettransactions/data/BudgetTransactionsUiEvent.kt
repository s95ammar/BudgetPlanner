package com.s95ammar.budgetplanner.ui.appscreens.dashboard.pager.budgettransactions.data

import com.s95ammar.budgetplanner.ui.appscreens.dashboard.common.data.BudgetTransaction
import com.s95ammar.budgetplanner.ui.common.LoadingState

sealed class BudgetTransactionsUiEvent {
    class DisplayLoadingState(val loadingState: LoadingState): BudgetTransactionsUiEvent()
    class ShowBottomSheet(val budgetTransaction: BudgetTransaction): BudgetTransactionsUiEvent()
    class NavigateToEditBudgetTransaction(val periodId: Int, val budgetTransactionId: Int) : BudgetTransactionsUiEvent()
}
