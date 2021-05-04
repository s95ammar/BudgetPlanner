package com.s95ammar.budgetplanner.ui.appscreens.dashboard

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.viewModelScope
import com.s95ammar.budgetplanner.models.repository.PeriodRepository
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.common.data.PeriodSimple
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.data.CurrentPeriodHeaderBundle
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.data.DashboardFabState
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.data.IntDashboardFabType
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.pager.IntDashboardTab
import com.s95ammar.budgetplanner.ui.common.IntLoadingType
import com.s95ammar.budgetplanner.ui.common.LoadingState
import com.s95ammar.budgetplanner.util.INVALID
import com.s95ammar.budgetplanner.util.lifecycleutil.EventMutableLiveData
import com.s95ammar.budgetplanner.util.lifecycleutil.LoaderMutableLiveData
import com.s95ammar.budgetplanner.util.lifecycleutil.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.data.DashboardUiEvent as UiEvent

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: PeriodRepository,
    private val savedStateHandle: SavedStateHandle,
    private val locale: Locale
) : ViewModel() {

    private val _selectedTab = MutableLiveData<@IntDashboardTab Int>()
    private val _allPeriods = LoaderMutableLiveData<List<PeriodSimple>> { loadAllPeriods() }
    private val _currentPeriodBundle = MediatorLiveData<CurrentPeriodHeaderBundle>().apply {
        addSource(_allPeriods.distinctUntilChanged()) { value = createCurrentPeriodHeaderBundle(it.lastOrNull()) }
    }
    private val _performUiEvent = EventMutableLiveData<UiEvent>()

    val currentPeriodBundle = _currentPeriodBundle.asLiveData()
    val fabState = MediatorLiveData<DashboardFabState>().apply {
        fun update() {
            val isPeriodAvailable = _currentPeriodBundle.value?.period != null
            value = createDashboardFabState(_selectedTab.value ?: Int.INVALID, isPeriodAvailable)
        }
        addSource(_selectedTab) { update() }
        addSource(_currentPeriodBundle) { update() }
    }.distinctUntilChanged()
    val dashboardTabs = MutableLiveData(IntDashboardTab.values()).asLiveData()
    val performUiEvent = _performUiEvent.asEventLiveData()

    fun onTabSelected(position: Int) {
        _selectedTab.value = position
    }

    fun onNextPeriodClick() {
        val currentPeriod = _currentPeriodBundle.value?.period ?: return
        val allPeriods = _allPeriods.value ?: return

        val currentPeriodIndex = allPeriods.indexOf(currentPeriod)
        if (currentPeriodIndex != Int.INVALID && currentPeriodIndex != allPeriods.lastIndex) {
            val newCurrentPeriod = allPeriods[currentPeriodIndex + 1]
            _currentPeriodBundle.value = createCurrentPeriodHeaderBundle(newCurrentPeriod)
        }
    }

    fun onPreviousPeriodClick() {
        val currentPeriod = _currentPeriodBundle.value?.period ?: return
        val allPeriods = _allPeriods.value ?: return

        val currentPeriodIndex = allPeriods.indexOf(currentPeriod)
        if (currentPeriodIndex != Int.INVALID && currentPeriodIndex != 0) {
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

    fun onEditSelectedPeriod() {
        _currentPeriodBundle.value?.period?.let { period ->
            _performUiEvent.call(UiEvent.NavigateToEditPeriod(period))
        }
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

    private fun createDashboardFabState(@IntDashboardTab currentTab: Int, isPeriodAvailable: Boolean): DashboardFabState {
        @IntDashboardFabType val type = if (!isPeriodAvailable) IntDashboardFabType.FAB_NONE else when (currentTab) {
            IntDashboardTab.TAB_BUDGET -> IntDashboardFabType.FAB_EDIT
            IntDashboardTab.TAB_BUDGET_TRANSACTIONS,
            IntDashboardTab.TAB_SAVINGS -> IntDashboardFabType.FAB_ADD
            else -> IntDashboardFabType.FAB_NONE
        }

        return DashboardFabState(type, currentTab)
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