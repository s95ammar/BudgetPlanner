package com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.periodcreateedit.subscreens.categoriesselection

import androidx.lifecycle.ViewModel
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.common.data.PeriodicCategory
import com.s95ammar.budgetplanner.util.lifecycleutil.EventMutableLiveData
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.periodcreateedit.subscreens.categoriesselection.data.PeriodCategoriesSelectionUiEvent as UiEvent

class PeriodCategoriesSelectionViewModel : ViewModel() {

    private val _performUiEvent = EventMutableLiveData<UiEvent>()

    val performUiEvent = _performUiEvent.asEventLiveData()

    fun onCreateEditEstimate(periodicCategory: PeriodicCategory) {
        _performUiEvent.call(UiEvent.NavigateToCreateEditEstimate(periodicCategory))
    }

    fun onChangeCurrency(periodicCategory: PeriodicCategory) {
        _performUiEvent.call(UiEvent.NavigateToCurrencySelection(periodicCategory))
    }

    fun onBack() {
        _performUiEvent.call(UiEvent.Exit)
    }
}