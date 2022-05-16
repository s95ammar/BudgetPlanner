package com.s95ammar.budgetplanner.ui.appscreens.categories.data

import com.s95ammar.budgetplanner.ui.appscreens.categories.common.data.Category
import com.s95ammar.budgetplanner.ui.common.LoadingState

sealed class CategoriesUiEvent {
    class DisplayLoadingState(val loadingState: LoadingState): CategoriesUiEvent()
    class NavigateToCreateEditCategory(val categoryId: Int): CategoriesUiEvent()
    class ShowBottomSheet(val category: Category): CategoriesUiEvent()
}
