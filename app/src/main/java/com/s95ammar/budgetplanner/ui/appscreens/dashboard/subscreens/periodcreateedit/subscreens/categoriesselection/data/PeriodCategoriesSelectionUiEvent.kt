package com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.periodcreateedit.subscreens.categoriesselection.data

import com.s95ammar.budgetplanner.ui.appscreens.dashboard.common.data.PeriodicCategory

sealed class PeriodCategoriesSelectionUiEvent {
    class NavigateToCreateEditEstimate(val periodicCategory: PeriodicCategory) : PeriodCategoriesSelectionUiEvent()
    object Exit : PeriodCategoriesSelectionUiEvent()
}
