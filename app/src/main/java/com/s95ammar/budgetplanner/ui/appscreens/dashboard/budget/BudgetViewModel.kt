package com.s95ammar.budgetplanner.ui.appscreens.dashboard.budget

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.s95ammar.budgetplanner.models.mappers.PeriodRecordApiViewMapper
import com.s95ammar.budgetplanner.models.repository.LocalRepository
import com.s95ammar.budgetplanner.models.repository.RemoteRepository
import com.s95ammar.budgetplanner.models.view.PeriodRecordViewEntity
import com.s95ammar.budgetplanner.ui.appscreens.auth.common.LoadingState
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.budget.data.PeriodRecordsNavigationBundle
import com.s95ammar.budgetplanner.util.lifecycleutil.EventMutableLiveData
import com.s95ammar.budgetplanner.util.lifecycleutil.asLiveData
import kotlinx.coroutines.launch

class BudgetViewModel @ViewModelInject constructor(
    private val localRepository: LocalRepository,
    private val remoteRepository: RemoteRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _currentPeriodId = MutableLiveData<Int>()
    private val _periodRecords = MutableLiveData<List<PeriodRecordViewEntity>>()
    private val _displayLoadingState = EventMutableLiveData<LoadingState>(LoadingState.Cold)
    private val _onNavigateToPeriodRecords = EventMutableLiveData<PeriodRecordsNavigationBundle>()

    val currentPeriodId = _currentPeriodId.asLiveData()
    val periodRecords = _periodRecords.asLiveData()
    val displayLoadingState = _displayLoadingState.asEventLiveData()
    val onNavigateToPeriodRecords = _onNavigateToPeriodRecords.asEventLiveData()

    fun setAndLoadCurrentPeriod(periodId: Int) {
        if (_currentPeriodId.value == periodId) return

        _currentPeriodId.value = periodId
        loadPeriodRecords()
    }

    fun refresh() {
        loadPeriodRecords()
    }

    fun onNavigateToPeriodRecords() {
        val existingCategoryIds = _periodRecords.value.orEmpty().map { it.categoryId }
        val periodId = _currentPeriodId.value ?: return
        _onNavigateToPeriodRecords.call(PeriodRecordsNavigationBundle(existingCategoryIds, periodId))
    }

    private fun loadPeriodRecords() {
        val periodId = _currentPeriodId.value ?: return

        _displayLoadingState.call(LoadingState.Loading)
        viewModelScope.launch {
            remoteRepository.getPeriodRecordsForPeriod(periodId)
                .onSuccess { periodRecordApiEntities ->
                    _periodRecords.value = periodRecordApiEntities.orEmpty().mapNotNull { PeriodRecordApiViewMapper.toViewEntity(it) }
                    _displayLoadingState.call(LoadingState.Success)
                }
                .onError { _displayLoadingState.call(LoadingState.Error(it)) }
        }
    }
}