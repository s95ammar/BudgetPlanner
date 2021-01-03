package com.s95ammar.budgetplanner.ui.appscreens.categories.categories.data

import com.s95ammar.budgetplanner.models.view.CategoryViewEntity
import com.s95ammar.budgetplanner.ui.appscreens.auth.common.LoadingState

sealed class CategoriesUiEvent {
    class DisplayLoadingState(val loadingState: LoadingState): CategoriesUiEvent()
    class OnNavigateToEditCategory(val categoryId: Int): CategoriesUiEvent()
    class ShowBottomSheet(val category: CategoryViewEntity): CategoriesUiEvent()
}
