package com.s95ammar.budgetplanner.ui.appscreens.categories.data

import com.s95ammar.budgetplanner.ui.appscreens.auth.common.LoadingState
import com.s95ammar.budgetplanner.ui.appscreens.categories.common.data.CategoryViewEntity

sealed class CategoriesUiEvent {
    class DisplayLoadingState(val loadingState: LoadingState): CategoriesUiEvent()
    class OnNavigateToEditCategory(val categoryId: Int): CategoriesUiEvent()
    class ShowBottomSheet(val category: CategoryViewEntity): CategoriesUiEvent()
}
