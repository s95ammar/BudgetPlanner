package com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.periods

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.s95ammar.budgetplanner.models.repository.PeriodRepository
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.common.data.PeriodSimpleViewEntity
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.periods.data.PeriodsUiEvent
import com.s95ammar.budgetplanner.ui.common.LoadingState
import com.s95ammar.budgetplanner.util.lifecycleutil.EventMutableLiveData
import com.s95ammar.budgetplanner.util.lifecycleutil.EventMutableLiveDataVoid
import com.s95ammar.budgetplanner.util.lifecycleutil.LoaderMutableLiveData
import com.s95ammar.budgetplanner.util.lifecycleutil.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PeriodsViewModel @Inject constructor(
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
        repository.deletePeriod(id)
            .onStart {
                _performUiEvent.call(PeriodsUiEvent.DisplayLoadingState(LoadingState.Loading))
            }
            .catch {
                _performUiEvent.call(PeriodsUiEvent.DisplayLoadingState(LoadingState.Error(it)))
            }
            .collect { _onPeriodDeleted.call() }
    }

    private fun loadAllPeriods() {
        viewModelScope.launch {
            repository.getAllUserPeriods()
                .onStart {
                    _performUiEvent.call(PeriodsUiEvent.DisplayLoadingState(LoadingState.Loading))
                }
                .catch {
                    _performUiEvent.call(PeriodsUiEvent.DisplayLoadingState(LoadingState.Error(it)))
                }
                .collect { periodApiEntities ->
/*
                    val periods = periodApiEntities.mapNotNull { apiEntity -> PeriodSimpleViewEntity.ApiMapper.toViewEntity(apiEntity) }
                    _allPeriods.value = periods
                    _performUiEvent.call(PeriodsUiEvent.DisplayLoadingState(LoadingState.Success))
*/
                }
        }
    }

}