package com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.periods

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.s95ammar.budgetplanner.models.repository.PeriodRepository
import com.s95ammar.budgetplanner.ui.appscreens.auth.common.LoadingState
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.common.data.PeriodSimpleViewEntity
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.periods.data.PeriodsUiEvent
import com.s95ammar.budgetplanner.util.lifecycleutil.EventMutableLiveData
import com.s95ammar.budgetplanner.util.lifecycleutil.EventMutableLiveDataVoid
import com.s95ammar.budgetplanner.util.lifecycleutil.LoaderMutableLiveData
import com.s95ammar.budgetplanner.util.lifecycleutil.asLiveData
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class PeriodsViewModel @ViewModelInject constructor(
    private val repository: PeriodRepository
) : ViewModel() {

    private val _allPeriods = LoaderMutableLiveData<List<PeriodSimpleViewEntity>> { loadAllPeriods() }
    private val _onPeriodDeleted = EventMutableLiveDataVoid()
    private val _performUiEvent = EventMutableLiveData<PeriodsUiEvent>()

    val allPeriods = _allPeriods.asLiveData()
    val onPeriodDeleted = _onPeriodDeleted.asEventLiveData()
    val performUiEvent = _performUiEvent.asEventLiveData()

    fun refresh() {
        loadAllPeriods()
    }

    fun onPeriodItemClick(position: Int) {
        _allPeriods.value?.getOrNull(position)?.let { period ->
            _performUiEvent.call(PeriodsUiEvent.OnNavigateToEditPeriod(period.id))
        }
    }

    fun onPeriodItemLongClick(position: Int) {
        _allPeriods.value?.getOrNull(position)?.let { period ->
            _performUiEvent.call(PeriodsUiEvent.ShowBottomSheet(period))
        }
    }

    fun deletePeriod(id: Int) = viewModelScope.launch {
        _performUiEvent.call(PeriodsUiEvent.DisplayLoadingState(LoadingState.Loading))
        repository.deletePeriod(id)
            .catch { _performUiEvent.call(PeriodsUiEvent.DisplayLoadingState(LoadingState.Error(it))) }
            .collect { _onPeriodDeleted.call() }
    }

    private fun loadAllPeriods() {
        viewModelScope.launch {
            _performUiEvent.call(PeriodsUiEvent.DisplayLoadingState(LoadingState.Loading))
            repository.getAllUserPeriods()
                .catch { _performUiEvent.call(PeriodsUiEvent.DisplayLoadingState(LoadingState.Error(it))) }
                .collect { periodApiEntities ->
                    val periods = periodApiEntities.mapNotNull { apiEntity -> PeriodSimpleViewEntity.ApiMapper.toViewEntity(apiEntity) }
                    _allPeriods.value = periods
                    _performUiEvent.call(PeriodsUiEvent.DisplayLoadingState(LoadingState.Success))
                }
        }
    }

}