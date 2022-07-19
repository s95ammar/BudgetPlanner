package com.s95ammar.budgetplanner.ui.appscreens.categories.subscreens.createedit.data

import com.s95ammar.budgetplanner.ui.common.LoadingState

sealed class CategoryCreateEditUiEvent {
    data class DisplayLoadingState(val loadingState: LoadingState): CategoryCreateEditUiEvent()
    object Exit: CategoryCreateEditUiEvent()
}
