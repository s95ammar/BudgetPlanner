package com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.periodcreateedit.subscreens.categoriesselection.subscreens.data

sealed class PeriodCategoryEstimateCreateEditUiEvent {
    class SetResult(val estimate: Double?) : PeriodCategoryEstimateCreateEditUiEvent()
    object Exit : PeriodCategoryEstimateCreateEditUiEvent()
}
