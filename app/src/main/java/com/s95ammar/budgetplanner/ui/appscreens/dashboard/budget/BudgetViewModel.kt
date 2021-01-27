package com.s95ammar.budgetplanner.ui.appscreens.dashboard.budget

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.s95ammar.budgetplanner.models.repository.LocalRepository
import com.s95ammar.budgetplanner.models.repository.RemoteRepository
import com.s95ammar.budgetplanner.models.view.PeriodRecordViewEntity
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.budget.data.BudgetUiEvent
import com.s95ammar.budgetplanner.util.lifecycleutil.EventMutableLiveData
import com.s95ammar.budgetplanner.util.lifecycleutil.asLiveData

class BudgetViewModel @ViewModelInject constructor(
    private val localRepository: LocalRepository,
    private val remoteRepository: RemoteRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _periodIdToPeriodRecordsMap = MutableLiveData(mutableMapOf<Int, List<PeriodRecordViewEntity>>())

    private val _currentPeriodId = MutableLiveData<Int>()
    private val _resultPeriodRecords = MediatorLiveData<List<PeriodRecordViewEntity>>().apply {
        addSource(_periodIdToPeriodRecordsMap) { map ->
            _currentPeriodId.value?.let { periodId ->
                value = map[periodId].orEmpty().filter { it.isSelected }
            }
        }
    }
    private val _performUiEvent = EventMutableLiveData<BudgetUiEvent>()

    val currentPeriodId = _currentPeriodId.asLiveData()
    val resultPeriodRecords = _resultPeriodRecords.asLiveData()
    val performUiEvent = _performUiEvent.asEventLiveData()

    fun onSelectedPeriodChanged(periodId: Int) {
        if (_currentPeriodId.value == periodId) return

        _currentPeriodId.value = periodId
        _resultPeriodRecords.value = _periodIdToPeriodRecordsMap.value?.get(periodId).orEmpty().filter { it.isSelected }
    }

    fun onPeriodRecordsLoaded(periodId: Int, periodRecords: List<PeriodRecordViewEntity>) {
        _periodIdToPeriodRecordsMap.value = _periodIdToPeriodRecordsMap.value?.apply { set(periodId, periodRecords) }
    }

    fun onEditPeriod() {
        _currentPeriodId.value?.let { _performUiEvent.call(BudgetUiEvent.OnEditPeriod(it)) }
    }

}