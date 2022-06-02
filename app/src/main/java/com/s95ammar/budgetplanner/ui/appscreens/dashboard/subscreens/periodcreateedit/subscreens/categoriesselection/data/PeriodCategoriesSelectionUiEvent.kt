package com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.periodcreateedit.subscreens.categoriesselection.data

import com.s95ammar.budgetplanner.ui.appscreens.dashboard.common.data.CategoryOfPeriod

sealed class PeriodCategoriesSelectionUiEvent {
    class NavigateToCurrencySelection(
        val categoryOfPeriod: CategoryOfPeriod,
        val isBackAllowed: Boolean
    ) : PeriodCategoriesSelectionUiEvent()
    class NavigateToCreateEditEstimate(val categoryOfPeriod: CategoryOfPeriod) : PeriodCategoriesSelectionUiEvent()
    object Exit : PeriodCategoriesSelectionUiEvent()
}
