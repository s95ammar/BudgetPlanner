package com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.periodcreateedit.subscreens.categoriesselection

import androidx.lifecycle.ViewModel
import com.s95ammar.budgetplanner.util.lifecycleutil.EventMutableLiveData
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.periodcreateedit.subscreens.categoriesselection.data.PeriodCategoriesSelectionUiEvent as UiEvent

class PeriodCategoriesSelectionViewModel : ViewModel() {

    private val _performUiEvent = EventMutableLiveData<UiEvent>()

    val performUiEvent = _performUiEvent.asEventLiveData()


    fun onBack() {
        _performUiEvent.call(UiEvent.Exit)
    }
}