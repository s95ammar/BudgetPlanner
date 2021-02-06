package com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budget

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.s95ammar.budgetplanner.models.repository.PeriodRepository
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budget.data.BudgetUiEvent
import com.s95ammar.budgetplanner.util.lifecycleutil.EventMutableLiveData
import com.s95ammar.budgetplanner.util.lifecycleutil.asLiveData

class BudgetViewModel @ViewModelInject constructor(
    private val repository: PeriodRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _currentPeriodId = MutableLiveData<Int>()

    private val _performUiEvent = EventMutableLiveData<BudgetUiEvent>()

    val currentPeriodId = _currentPeriodId.asLiveData()
    val performUiEvent = _performUiEvent.asEventLiveData()

    fun onSelectedPeriodChanged(periodId: Int) {
        _currentPeriodId.value = periodId
    }

    fun onEditPeriod() {
        _currentPeriodId.value?.let { _performUiEvent.call(BudgetUiEvent.OnEditPeriod(it)) }
    }

}