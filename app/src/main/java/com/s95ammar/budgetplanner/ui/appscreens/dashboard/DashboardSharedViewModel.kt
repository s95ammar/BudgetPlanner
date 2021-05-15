package com.s95ammar.budgetplanner.ui.appscreens.dashboard

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.s95ammar.budgetplanner.models.repository.PeriodRepository
import com.s95ammar.budgetplanner.util.INVALID
import com.s95ammar.budgetplanner.util.lifecycleutil.EventMutableLiveData
import com.s95ammar.budgetplanner.util.lifecycleutil.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.data.DashboardUiEvent as UiEvent

@HiltViewModel
class DashboardSharedViewModel @Inject constructor(
    private val repository: PeriodRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _selectedPeriodId = MutableLiveData(Int.INVALID)
    private val _performUiEvent = EventMutableLiveData<UiEvent>()

    val selectedPeriodId = _selectedPeriodId.asLiveData()
    val performUiEvent = _performUiEvent.asEventLiveData()

    fun onPeriodChanged(periodId: Int?) {
        _selectedPeriodId.value = periodId ?: Int.INVALID
    }

    fun onNavigateToEditBudgetTransaction(periodId: Int, budgetTransactionId: Int) {
        _performUiEvent.call(UiEvent.NavigateToEditBudgetTransaction(periodId, budgetTransactionId))
    }
}
