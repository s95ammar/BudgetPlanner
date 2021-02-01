package com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.periods

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.s95ammar.budgetplanner.models.mappers.PeriodSimpleApiViewMapper
import com.s95ammar.budgetplanner.models.repository.LocalRepository
import com.s95ammar.budgetplanner.models.repository.RemoteRepository
import com.s95ammar.budgetplanner.models.view.PeriodSimpleViewEntity
import com.s95ammar.budgetplanner.ui.appscreens.auth.common.LoadingState
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.periods.data.PeriodsUiEvent
import com.s95ammar.budgetplanner.util.lifecycleutil.EventMutableLiveData
import com.s95ammar.budgetplanner.util.lifecycleutil.EventMutableLiveDataVoid
import com.s95ammar.budgetplanner.util.lifecycleutil.LoaderMutableLiveData
import com.s95ammar.budgetplanner.util.lifecycleutil.asLiveData
import kotlinx.coroutines.launch

class PeriodsViewModel @ViewModelInject constructor(
    private val localRepository: LocalRepository,
    private val remoteRepository: RemoteRepository
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
        remoteRepository.deletePeriod(id)
            .onSuccess { _onPeriodDeleted.call() }
            .onError { _performUiEvent.call(PeriodsUiEvent.DisplayLoadingState(LoadingState.Error(it))) }
    }

    private fun loadAllPeriods() {
        viewModelScope.launch {
            _performUiEvent.call(PeriodsUiEvent.DisplayLoadingState(LoadingState.Loading))
            remoteRepository.getAllUserPeriods()
                .onSuccess { periodApiEntities ->
                    val periods = periodApiEntities.orEmpty().mapNotNull { apiEntity -> PeriodSimpleApiViewMapper.toViewEntity(apiEntity) }
                    _allPeriods.value = periods
                    _performUiEvent.call(PeriodsUiEvent.DisplayLoadingState(LoadingState.Success))
                }
                .onError { _performUiEvent.call(PeriodsUiEvent.DisplayLoadingState(LoadingState.Error(it))) }
        }
    }

}