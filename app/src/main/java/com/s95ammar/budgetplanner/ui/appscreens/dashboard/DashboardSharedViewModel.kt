package com.s95ammar.budgetplanner.ui.appscreens.dashboard

import androidx.lifecycle.*
import com.s95ammar.budgetplanner.models.repository.PeriodRepository
import com.s95ammar.budgetplanner.ui.appscreens.auth.common.LoadingState
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.common.data.BudgetTransactionViewEntity
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.common.data.BudgetTransactionViewEntity.ApiMapper.mapToViewEntityList
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.common.data.PeriodicCategoryViewEntity
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.common.data.PeriodicCategoryViewEntity.ApiMapper.mapToViewEntityList
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.data.DashboardUiEvent
import com.s95ammar.budgetplanner.ui.common.IntLoadingType
import com.s95ammar.budgetplanner.util.NO_ITEM
import com.s95ammar.budgetplanner.util.lifecycleutil.EventMutableLiveData
import com.s95ammar.budgetplanner.util.lifecycleutil.MediatorLiveData
import com.s95ammar.budgetplanner.util.lifecycleutil.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardSharedViewModel @Inject constructor(
    private val repository: PeriodRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _selectedPeriodId = MediatorLiveData(Int.NO_ITEM).apply {
        addSource(this) { loadPeriod(it) }
    }
    private val _currentPeriodicRecords = MutableLiveData<List<PeriodicCategoryViewEntity>>()
    private val _currentBudgetTransactions = MutableLiveData<List<BudgetTransactionViewEntity>>()
    private val _performDashboardUiEvent = EventMutableLiveData<DashboardUiEvent>()

    val isPeriodAvailable = _selectedPeriodId.map { it != Int.NO_ITEM }
    val currentPeriodicRecords = _currentPeriodicRecords.asLiveData()
    val currentBudgetTransactions = _currentBudgetTransactions.asLiveData()
    val performDashboardUiEvent = _performDashboardUiEvent.asEventLiveData()

    fun onPeriodChanged(periodId: Int?) {
        periodId?.let { _selectedPeriodId.value = it }
    }

    fun onEditSelectedPeriod() {
        _selectedPeriodId.value?.takeIf { it != Int.NO_ITEM }?.let { periodId ->
            _performDashboardUiEvent.call(DashboardUiEvent.NavigateToEditPeriod(periodId))
        }
    }

    fun onRefresh() {
        _selectedPeriodId.value?.let { id ->
            loadPeriod(id)
        }
    }

    private fun loadPeriod(periodId: Int) = viewModelScope.launch {
        if (periodId == Int.NO_ITEM) return@launch

        repository.getPeriod(
            id = periodId,
            includePeriodicCategories = true,
            includeBudgetTransactions = true,
            includeSavings = true
        ).onStart {
            _performDashboardUiEvent.call(DashboardUiEvent.DisplayLoadingState(LoadingState.Loading, IntLoadingType.SWIPE_TO_REFRESH))
        }.catch {
            _performDashboardUiEvent.call(DashboardUiEvent.DisplayLoadingState(LoadingState.Error(it), IntLoadingType.SWIPE_TO_REFRESH))
        }.collect { period ->
            _currentPeriodicRecords.value = period.periodicCategories.orEmpty().mapToViewEntityList().filter { it.isSelected }
            _currentBudgetTransactions.value = period.budgetTransactions.orEmpty().mapToViewEntityList()
            _performDashboardUiEvent.call(DashboardUiEvent.DisplayLoadingState(LoadingState.Success, IntLoadingType.SWIPE_TO_REFRESH))
        }
    }
}