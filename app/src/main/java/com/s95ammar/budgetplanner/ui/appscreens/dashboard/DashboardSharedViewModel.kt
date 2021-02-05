package com.s95ammar.budgetplanner.ui.appscreens.dashboard

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.s95ammar.budgetplanner.models.repository.PeriodRepository
import com.s95ammar.budgetplanner.ui.appscreens.auth.common.LoadingState
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.common.data.PeriodViewEntity
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.common.data.PeriodicCategoryViewEntity
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.data.DashboardUiEvent
import com.s95ammar.budgetplanner.ui.common.IntLoadingType
import com.s95ammar.budgetplanner.util.lifecycleutil.EventMutableLiveData
import com.s95ammar.budgetplanner.util.lifecycleutil.asLiveData
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class DashboardSharedViewModel @ViewModelInject constructor(
    private val repository: PeriodRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _selectedPeriodId = MutableLiveData<Int>()
    private val _performDashboardUiEvent = EventMutableLiveData<DashboardUiEvent>()
    private val _navigateToEditPeriod = EventMutableLiveData<Int>()
    private val _onPeriodicCategoriesLoaded = EventMutableLiveData<Pair<Int, List<PeriodicCategoryViewEntity>>>()

    val selectedPeriodId = _selectedPeriodId.asLiveData()
    val navigateToEditPeriod = _navigateToEditPeriod.asEventLiveData()
    val onPeriodicCategoriesLoaded = _onPeriodicCategoriesLoaded.asEventLiveData()
    val performDashboardUiEvent = _performDashboardUiEvent.asEventLiveData()

    fun onPeriodChanged(periodId: Int?) {
        periodId?.let { _selectedPeriodId.value = it }
    }

    fun onEditPeriod(periodId: Int) {
        _navigateToEditPeriod.call(periodId)
    }

    fun onRefresh() {
        _selectedPeriodId.value?.let { id ->
            loadPeriod(id)
        }
    }

    private fun loadPeriod(periodId: Int) = viewModelScope.launch {
        repository.getPeriod(
            id = periodId,
            includePeriodicCategories = true,
            includeBudgetTransactions = true,
            includeSavings = true
        ).onStart {
            _performDashboardUiEvent.call(DashboardUiEvent.DisplayLoadingState(LoadingState.Loading, IntLoadingType.SWIPE_TO_REFRESH))
        }.catch {
            _performDashboardUiEvent.call(DashboardUiEvent.DisplayLoadingState(LoadingState.Error(it), IntLoadingType.SWIPE_TO_REFRESH))
        }.collect { periodApiEntity ->
            PeriodViewEntity.ApiMapper.toViewEntity(periodApiEntity)?.let { period ->
                _onPeriodicCategoriesLoaded.call(periodId to period.periodicCategories)
            }
            _performDashboardUiEvent.call(DashboardUiEvent.DisplayLoadingState(LoadingState.Success, IntLoadingType.SWIPE_TO_REFRESH))
        }
    }
}