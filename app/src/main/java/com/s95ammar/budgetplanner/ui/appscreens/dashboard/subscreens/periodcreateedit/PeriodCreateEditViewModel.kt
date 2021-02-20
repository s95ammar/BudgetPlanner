package com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.periodcreateedit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.s95ammar.budgetplanner.models.repository.PeriodRepository
import com.s95ammar.budgetplanner.util.lifecycleutil.EventMutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.periodcreateedit.data.PeriodCreateEditUiEvent as UiEvent

@HiltViewModel
class PeriodCreateEditViewModel @Inject constructor(
    private val repository: PeriodRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _performUiEvent = EventMutableLiveData<UiEvent>()

    val performUiEvent = _performUiEvent.asEventLiveData()

    fun onChooseCategories() {
        _performUiEvent.call(UiEvent.ChooseCategories)
    }

    fun onBack() {
        _performUiEvent.call(UiEvent.Exit)
    }


}