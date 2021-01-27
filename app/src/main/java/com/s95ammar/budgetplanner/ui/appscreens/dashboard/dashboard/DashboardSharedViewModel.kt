package com.s95ammar.budgetplanner.ui.appscreens.dashboard.dashboard

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.s95ammar.budgetplanner.models.mappers.PeriodApiViewMapper
import com.s95ammar.budgetplanner.models.repository.LocalRepository
import com.s95ammar.budgetplanner.models.repository.RemoteRepository
import com.s95ammar.budgetplanner.models.view.PeriodRecordViewEntity
import com.s95ammar.budgetplanner.ui.appscreens.auth.common.LoadingState
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.dashboard.data.DashboardUiEvent
import com.s95ammar.budgetplanner.ui.common.IntLoadingType
import com.s95ammar.budgetplanner.util.lifecycleutil.EventMutableLiveData
import com.s95ammar.budgetplanner.util.lifecycleutil.asLiveData
import kotlinx.coroutines.launch

class DashboardSharedViewModel @ViewModelInject constructor(
    private val localRepository: LocalRepository,
    private val remoteRepository: RemoteRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _selectedPeriodId = MutableLiveData<Int>()
    private val _performDashboardUiEvent = EventMutableLiveData<DashboardUiEvent>()
    private val _navigateToEditPeriod = EventMutableLiveData<Int>()
    private val _onPeriodRecordsLoaded = EventMutableLiveData<Pair<Int, List<PeriodRecordViewEntity>>>()

    val selectedPeriodId = _selectedPeriodId.asLiveData()
    val navigateToEditPeriod = _navigateToEditPeriod.asEventLiveData()
    val onPeriodRecordsLoaded = _onPeriodRecordsLoaded.asEventLiveData()
    val performDashboardUiEvent = _performDashboardUiEvent.asEventLiveData()

    fun onPeriodChanged(periodId: Int) {
        _selectedPeriodId.value = periodId
        loadPeriod(periodId)
    }

    fun onEditPeriod(periodId: Int) {
        _navigateToEditPeriod.call(periodId)
    }

    fun onRefresh() {
        _selectedPeriodId.value?.let { id ->
            loadPeriod(id)
        }
    }

    private fun loadPeriod(periodId: Int) {
        viewModelScope.launch {
            _performDashboardUiEvent.call(DashboardUiEvent.DisplayLoadingState(LoadingState.Loading, IntLoadingType.SWIPE_TO_REFRESH))
            remoteRepository.getPeriod(
                id = periodId,
                includePeriodRecords = true,
                includeBudgetTransactions = true,
                includeSavings = true
            ).onSuccess { periodApiEntity ->
                PeriodApiViewMapper.toViewEntity(periodApiEntity)?.let { period ->
                    _onPeriodRecordsLoaded.call(periodId to period.periodRecords)
                }
                _performDashboardUiEvent.call(DashboardUiEvent.DisplayLoadingState(LoadingState.Success, IntLoadingType.SWIPE_TO_REFRESH))
            }.onError {
                _performDashboardUiEvent.call(DashboardUiEvent.DisplayLoadingState(LoadingState.Error(it), IntLoadingType.SWIPE_TO_REFRESH))
            }
        }

    }
}