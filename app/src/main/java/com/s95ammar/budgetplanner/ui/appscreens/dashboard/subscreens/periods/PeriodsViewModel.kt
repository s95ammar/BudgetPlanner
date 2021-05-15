package com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.periods

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.s95ammar.budgetplanner.models.repository.PeriodRepository
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.common.data.PeriodSimple
import com.s95ammar.budgetplanner.ui.common.LoadingState
import com.s95ammar.budgetplanner.util.lifecycleutil.EventMutableLiveData
import com.s95ammar.budgetplanner.util.lifecycleutil.LoaderMutableLiveData
import com.s95ammar.budgetplanner.util.lifecycleutil.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.periods.data.PeriodsUiEvent as UiEvent

@HiltViewModel
class PeriodsViewModel @Inject constructor(
    private val repository: PeriodRepository
) : ViewModel() {

    private val _allPeriods = LoaderMutableLiveData<List<PeriodSimple>> { loadAllPeriods() }
    private val _performUiEvent = EventMutableLiveData<UiEvent>()

    val allPeriods = _allPeriods.asLiveData()
    val performUiEvent = _performUiEvent.asEventLiveData()

    fun onPeriodItemClick(position: Int) {
        _allPeriods.value?.getOrNull(position)?.let { period ->
            _performUiEvent.call(UiEvent.ListenAndNavigateToEditPeriod(period))
        }
    }

    fun onPeriodItemLongClick(position: Int) {
        _allPeriods.value?.getOrNull(position)?.let { period ->
            _performUiEvent.call(UiEvent.ShowBottomSheet(period))
        }
    }

    fun deletePeriod(id: Int) = viewModelScope.launch {
        repository.deletePeriodFlow(id)
            .onStart {
                _performUiEvent.call(UiEvent.DisplayLoadingState(LoadingState.Loading))
            }
            .catch { throwable ->
                _performUiEvent.call(UiEvent.DisplayLoadingState(LoadingState.Error(throwable)))
            }
            .collect {
                _performUiEvent.call(UiEvent.DisplayLoadingState(LoadingState.Success))
            }
    }

    private fun loadAllPeriods() {
        viewModelScope.launch {
            repository.getAllUserPeriodsFlow()
                .onStart {
                    _performUiEvent.call(UiEvent.DisplayLoadingState(LoadingState.Loading))
                }
                .catch { throwable ->
                    _performUiEvent.call(UiEvent.DisplayLoadingState(LoadingState.Error(throwable)))
                }
                .collect { periodEntityList ->
                    _performUiEvent.call(UiEvent.DisplayLoadingState(LoadingState.Success))

                    if (periodEntityList.isEmpty()) {
                        _performUiEvent.call(UiEvent.Exit)
                    } else {
                        _allPeriods.value = periodEntityList.mapNotNull(PeriodSimple.Mapper::fromEntity)
                    }
                }
        }
    }

}