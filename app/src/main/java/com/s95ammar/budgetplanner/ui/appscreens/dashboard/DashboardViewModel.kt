package com.s95ammar.budgetplanner.ui.appscreens.dashboard

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.s95ammar.budgetplanner.models.repository.LocalRepository
import com.s95ammar.budgetplanner.models.repository.RemoteRepository
import com.s95ammar.budgetplanner.ui.appscreens.auth.common.LoadingState
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.common.data.PeriodSimpleViewEntity
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.data.CurrentPeriodBundle
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.data.DashboardUiEvent
import com.s95ammar.budgetplanner.ui.common.IntLoadingType
import com.s95ammar.budgetplanner.util.NO_ITEM
import com.s95ammar.budgetplanner.util.lifecycleutil.EventMutableLiveData
import com.s95ammar.budgetplanner.util.lifecycleutil.LoaderMutableLiveData
import com.s95ammar.budgetplanner.util.lifecycleutil.asLiveData
import kotlinx.coroutines.launch

class DashboardViewModel @ViewModelInject constructor(
    private val localRepository: LocalRepository,
    private val remoteRepository: RemoteRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _allPeriods = LoaderMutableLiveData<List<PeriodSimpleViewEntity>> { loadAllPeriods() }
    private val _currentPeriodBundle = MediatorLiveData<CurrentPeriodBundle>().apply {
        addSource(_allPeriods.distinctUntilChanged()) { value = createCurrentPeriodBundle(it.lastOrNull()) }
    }
    private val _performUiEvent = EventMutableLiveData<DashboardUiEvent>()

    val currentPeriodBundle = _currentPeriodBundle.asLiveData()
    val performUiEvent = _performUiEvent.asEventLiveData()

    fun onNextPeriodClick() {
        val currentPeriod = _currentPeriodBundle.value?.period ?: return
        val allPeriods = _allPeriods.value ?: return

        val currentPeriodIndex = allPeriods.indexOf(currentPeriod)
        if (currentPeriodIndex != Int.NO_ITEM && currentPeriodIndex != allPeriods.lastIndex)
            _currentPeriodBundle.value = createCurrentPeriodBundle(allPeriods[currentPeriodIndex + 1])
    }

    fun onPreviousPeriodClick() {
        val currentPeriod = _currentPeriodBundle.value?.period ?: return
        val allPeriods = _allPeriods.value ?: return

        val currentPeriodIndex = allPeriods.indexOf(currentPeriod)
        if (currentPeriodIndex != Int.NO_ITEM && currentPeriodIndex != 0)
            _currentPeriodBundle.value = createCurrentPeriodBundle(allPeriods[currentPeriodIndex - 1])
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

    private fun createCurrentPeriodBundle(currentPeriod: PeriodSimpleViewEntity?): CurrentPeriodBundle {
        var period: PeriodSimpleViewEntity? = null
        var isPreviousAvailable = false
        var isNextAvailable = false

        _allPeriods.value?.takeIf { it.isNotEmpty() }?.let { periods ->
            period = currentPeriod
            isPreviousAvailable = currentPeriod != periods.first()
            isNextAvailable = currentPeriod != periods.last()
        }

        return CurrentPeriodBundle(period, isPreviousAvailable, isNextAvailable)
    }

    private fun loadAllPeriods() {
        viewModelScope.launch {
            _performUiEvent.call(DashboardUiEvent.DisplayLoadingState(LoadingState.Loading, IntLoadingType.MAIN))
            remoteRepository.getAllUserPeriods()
                .onSuccess { periodsApiEntities ->
                    _allPeriods.value = periodsApiEntities.orEmpty()
                        .mapNotNull { apiEntity -> PeriodSimpleViewEntity.ApiMapper.toViewEntity(apiEntity) }
                    _performUiEvent.call(DashboardUiEvent.DisplayLoadingState(LoadingState.Success, IntLoadingType.MAIN))
                }
                .onError { throwable ->
                    _performUiEvent.call(DashboardUiEvent.DisplayLoadingState(LoadingState.Error(throwable), IntLoadingType.MAIN))
                }
        }
    }

}