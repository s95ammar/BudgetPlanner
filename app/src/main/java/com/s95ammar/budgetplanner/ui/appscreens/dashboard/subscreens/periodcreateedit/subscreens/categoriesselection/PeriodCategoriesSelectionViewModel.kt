package com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.periodcreateedit.subscreens.categoriesselection

import androidx.lifecycle.ViewModel
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.common.data.CategoryOfPeriod
import com.s95ammar.budgetplanner.util.lifecycleutil.EventMutableLiveData
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.periodcreateedit.subscreens.categoriesselection.data.PeriodCategoriesSelectionUiEvent as UiEvent

class PeriodCategoriesSelectionViewModel : ViewModel() {

    private val _performUiEvent = EventMutableLiveData<UiEvent>()

    val performUiEvent = _performUiEvent.asEventLiveData()

    fun onCreateEditEstimate(categoryOfPeriod: CategoryOfPeriod) {
        _performUiEvent.call(UiEvent.NavigateToCreateEditEstimate(categoryOfPeriod))
    }

    fun onChangeCurrency(categoryOfPeriod: CategoryOfPeriod) {
        _performUiEvent.call(UiEvent.NavigateToCurrencySelection(categoryOfPeriod, isBackAllowed = true))
    }

    fun onBack() {
        _performUiEvent.call(UiEvent.Exit)
    }
}