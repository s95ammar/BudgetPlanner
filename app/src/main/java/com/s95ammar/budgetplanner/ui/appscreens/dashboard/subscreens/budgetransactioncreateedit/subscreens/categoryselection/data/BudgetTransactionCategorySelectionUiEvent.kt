package com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgetransactioncreateedit.subscreens.categoryselection.data

import com.s95ammar.budgetplanner.ui.appscreens.categories.common.data.CategoryViewEntity
import com.s95ammar.budgetplanner.ui.common.LoadingState

sealed class BudgetTransactionCategorySelectionUiEvent {
    class DisplayLoadingState(val loadingState: LoadingState): BudgetTransactionCategorySelectionUiEvent()
    class SetResult(val category: CategoryViewEntity): BudgetTransactionCategorySelectionUiEvent()
    object Exit: BudgetTransactionCategorySelectionUiEvent()
}
