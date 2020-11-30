package com.s95ammar.budgetplanner.ui.appscreens.dashboard.budget

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.s95ammar.budgetplanner.models.mappers.PeriodApiViewMapper
import com.s95ammar.budgetplanner.models.repository.LocalRepository
import com.s95ammar.budgetplanner.models.repository.RemoteRepository
import com.s95ammar.budgetplanner.models.view.PeriodViewEntity
import com.s95ammar.budgetplanner.ui.appscreens.auth.common.LoadingState
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.budget.data.CurrentPeriodBundle
import com.s95ammar.budgetplanner.util.NO_ITEM
import com.s95ammar.budgetplanner.util.lifecycleutil.EventMutableLiveData
import com.s95ammar.budgetplanner.util.lifecycleutil.LoaderMutableLiveData
import com.s95ammar.budgetplanner.util.lifecycleutil.asLiveData
import kotlinx.coroutines.launch

class BudgetViewModel @ViewModelInject constructor(
    private val localRepository: LocalRepository,
    private val remoteRepository: RemoteRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _allPeriods = LoaderMutableLiveData<List<PeriodViewEntity>> { loadAllPeriods() }
    private val _displayLoadingState = EventMutableLiveData<LoadingState>(LoadingState.Cold)
    private val _currentPeriodBundle = MediatorLiveData<CurrentPeriodBundle>().apply {
        addSource(_allPeriods.distinctUntilChanged()) { value = createCurrentPeriodBundle(it.lastOrNull()) }
    }

    val currentPeriodBundle = _currentPeriodBundle.asLiveData()
    val displayLoadingState = _displayLoadingState.asEventLiveData()

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

    fun refresh() {
        loadAllPeriods()
    }

    private fun createCurrentPeriodBundle(currentPeriod: PeriodViewEntity?): CurrentPeriodBundle {
        var period: PeriodViewEntity? = null
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
            _displayLoadingState.call(LoadingState.Loading)
            remoteRepository.getAllUserPeriods()
                .onSuccess { periodsApiEntities ->
                    _allPeriods.value = periodsApiEntities.orEmpty().map { apiEntity -> PeriodApiViewMapper.toViewEntity(apiEntity) }
                    _displayLoadingState.call(LoadingState.Success)
                }
                .onError { _displayLoadingState.call(LoadingState.Error(it)) }
        }
    }

}