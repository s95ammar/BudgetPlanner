package com.s95ammar.budgetplanner.ui.appscreens.dashboard

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.viewModelScope
import com.s95ammar.budgetplanner.models.repository.PeriodRepository
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.common.data.PeriodSimple
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.data.CurrentPeriodHeaderBundle
import com.s95ammar.budgetplanner.ui.common.IntLoadingType
import com.s95ammar.budgetplanner.ui.common.LoadingState
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
import java.util.*
import javax.inject.Inject
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.data.DashboardUiEvent as UiEvent

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: PeriodRepository,
    private val savedStateHandle: SavedStateHandle,
    private val locale: Locale
) : ViewModel() {
    private val _allPeriods = LoaderMutableLiveData<List<PeriodSimple>> { loadAllPeriods() }
    private val _currentPeriodBundle = MediatorLiveData(createCurrentPeriodHeaderBundle(null)).apply {
        addSource(_allPeriods.distinctUntilChanged()) { value = createCurrentPeriodHeaderBundle(it.lastOrNull()) }
    }
    private val _performUiEvent = EventMutableLiveData<UiEvent>()

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
        _performUiEvent.call(UiEvent.NavigateToPeriodsList)
    }

    fun onAddPeriodClick() {
        _performUiEvent.call(UiEvent.NavigateToCreatePeriod)
    }

    fun refresh() {
        loadAllPeriods()
    }

    private fun createCurrentPeriodHeaderBundle(currentPeriod: PeriodSimple?): CurrentPeriodHeaderBundle {
        var isPreviousAvailable = false
        var isNextAvailable = false
        val allPeriods = _allPeriods.value

        if (!allPeriods.isNullOrEmpty() && currentPeriod != null) {
            isPreviousAvailable = allPeriods.first() != currentPeriod
            isNextAvailable = allPeriods.last() != currentPeriod
        }

        return CurrentPeriodHeaderBundle(currentPeriod, isPreviousAvailable, isNextAvailable)
    }

    private fun loadAllPeriods() {
        viewModelScope.launch {
            repository.getAllUserPeriodsFlow()
                .onStart {
                    _performUiEvent.call(UiEvent.DisplayLoadingState(LoadingState.Loading, IntLoadingType.MAIN))
                }
                .catch { throwable ->
                    _performUiEvent.call(UiEvent.DisplayLoadingState(LoadingState.Error(throwable), IntLoadingType.MAIN))
                }
                .collect { periodsEntityList ->
                    _allPeriods.value = periodsEntityList.mapNotNull(PeriodSimple.Mapper::fromEntity)
                    _performUiEvent.call(UiEvent.DisplayLoadingState(LoadingState.Success, IntLoadingType.MAIN))
                }
        }
    }

}