package com.s95ammar.budgetplanner.ui.appscreens.dashboard

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.viewModelScope
import com.s95ammar.budgetplanner.models.repository.PeriodRepository
import com.s95ammar.budgetplanner.ui.appscreens.auth.common.LoadingState
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.common.data.PeriodSimpleViewEntity
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.data.CurrentPeriodHeaderBundle
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.data.DashboardUiEvent
import com.s95ammar.budgetplanner.ui.common.IntLoadingType
import com.s95ammar.budgetplanner.util.NO_ITEM
import com.s95ammar.budgetplanner.util.lifecycleutil.EventMutableLiveData
import com.s95ammar.budgetplanner.util.lifecycleutil.LoaderMutableLiveData
import com.s95ammar.budgetplanner.util.lifecycleutil.MediatorLiveData
import com.s95ammar.budgetplanner.util.lifecycleutil.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: PeriodRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _allPeriods = LoaderMutableLiveData<List<PeriodSimpleViewEntity>> { loadAllPeriods() }
    private val _currentPeriodBundle = MediatorLiveData(createCurrentPeriodHeaderBundle(null)).apply {
        addSource(_allPeriods.distinctUntilChanged()) { value = createCurrentPeriodHeaderBundle(it.lastOrNull()) }
    }
    private val _performUiEvent = EventMutableLiveData<DashboardUiEvent>()

    val currentPeriodBundle = _currentPeriodBundle.asLiveData()
    val performUiEvent = _performUiEvent.asEventLiveData()

    fun onNextPeriodClick() {
        val currentPeriod = _currentPeriodBundle.value?.period ?: return
        val allPeriods = _allPeriods.value ?: return

        val currentPeriodIndex = allPeriods.indexOf(currentPeriod)
        if (currentPeriodIndex != Int.NO_ITEM && currentPeriodIndex != allPeriods.lastIndex) {
            val newCurrentPeriod = allPeriods[currentPeriodIndex + 1]
            _currentPeriodBundle.value = createCurrentPeriodHeaderBundle(newCurrentPeriod)
        }
    }

    fun onPreviousPeriodClick() {
        val currentPeriod = _currentPeriodBundle.value?.period ?: return
        val allPeriods = _allPeriods.value ?: return

        val currentPeriodIndex = allPeriods.indexOf(currentPeriod)
        if (currentPeriodIndex != Int.NO_ITEM && currentPeriodIndex != 0) {
            val newCurrentPeriod = allPeriods[currentPeriodIndex - 1]
            _currentPeriodBundle.value = createCurrentPeriodHeaderBundle(newCurrentPeriod)
        }
    }

    fun onPeriodNameClick() {
        _performUiEvent.call(DashboardUiEvent.NavigateToPeriodsList)
    }

    fun onAddPeriodClick() {
        _performUiEvent.call(DashboardUiEvent.NavigateToCreatePeriod)
    }

    fun refresh() {
        loadAllPeriods()
    }

    private fun createCurrentPeriodHeaderBundle(currentPeriod: PeriodSimpleViewEntity?): CurrentPeriodHeaderBundle {
        var period: PeriodSimpleViewEntity? = null
        var isPreviousAvailable = false
        var isNextAvailable = false

        _allPeriods.value?.takeIf { it.isNotEmpty() }?.let { periods ->
            period = currentPeriod
            isPreviousAvailable = currentPeriod != periods.first()
            isNextAvailable = currentPeriod != periods.last()
        }

        return CurrentPeriodHeaderBundle(period, isPreviousAvailable, isNextAvailable)
    }

    private fun loadAllPeriods() {
        viewModelScope.launch {
            repository.getAllUserPeriods()
                .onStart {
                    _performUiEvent.call(DashboardUiEvent.DisplayLoadingState(LoadingState.Loading, IntLoadingType.MAIN))
                }
                .catch { throwable ->
                    _performUiEvent.call(DashboardUiEvent.DisplayLoadingState(LoadingState.Error(throwable), IntLoadingType.MAIN))
                }
                .collect { periodsApiEntities ->
                    _allPeriods.value = periodsApiEntities.orEmpty()
                        .mapNotNull { apiEntity -> PeriodSimpleViewEntity.ApiMapper.toViewEntity(apiEntity) }
                    _performUiEvent.call(DashboardUiEvent.DisplayLoadingState(LoadingState.Success, IntLoadingType.MAIN))
                }
        }
    }

}