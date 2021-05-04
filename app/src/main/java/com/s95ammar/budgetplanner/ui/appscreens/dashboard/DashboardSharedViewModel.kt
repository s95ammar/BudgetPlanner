package com.s95ammar.budgetplanner.ui.appscreens.dashboard

import androidx.lifecycle.*
import com.s95ammar.budgetplanner.models.repository.PeriodRepository
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.common.data.BudgetTransactionViewEntity
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.common.data.PeriodicCategory
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.data.DashboardUiEvent
import com.s95ammar.budgetplanner.util.INVALID
import com.s95ammar.budgetplanner.util.lifecycleutil.EventMutableLiveData
import com.s95ammar.budgetplanner.util.lifecycleutil.MediatorLiveData
import com.s95ammar.budgetplanner.util.lifecycleutil.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardSharedViewModel @Inject constructor(
    private val repository: PeriodRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _selectedPeriodId = MediatorLiveData(Int.INVALID).apply {
        addSource(this) { loadPeriod(it) }
    }
    private val _currentPeriodicCategories = MutableLiveData<List<PeriodicCategory>>()
    private val _currentBudgetTransactions = MutableLiveData<List<BudgetTransactionViewEntity>>()
    private val _performDashboardUiEvent = EventMutableLiveData<DashboardUiEvent>()

    val currentPeriodicCategories = _currentPeriodicCategories.asLiveData()
    val currentBudgetTransactions = _currentBudgetTransactions.asLiveData()
    val performDashboardUiEvent = _performDashboardUiEvent.asEventLiveData()

    fun refresh() {
        _selectedPeriodId.value?.let { id ->
            loadPeriod(id)
        }
    }

    fun onPeriodChanged(periodId: Int?) {
        _selectedPeriodId.value = periodId ?: Int.INVALID
    }

    fun onAddBudgetTransaction() {
        _performDashboardUiEvent.call(DashboardUiEvent.NavigateToCreateBudgetTransaction(Int.INVALID))
    }

    private fun loadPeriod(periodId: Int) = viewModelScope.launch {
        if (periodId == Int.INVALID) return@launch

/*
        repository.getPeriodJoinEntityListFlow(id = periodId)
            .onStart {
                _performDashboardUiEvent.call(DashboardUiEvent.DisplayLoadingState(LoadingState.Loading, IntLoadingType.SWIPE_TO_REFRESH))
            }.catch {
                _performDashboardUiEvent.call(DashboardUiEvent.DisplayLoadingState(LoadingState.Error(it), IntLoadingType.SWIPE_TO_REFRESH))
            }.collect { period ->
*/
/*
                _currentPeriodicRecords.value = period.periodicCategories.orEmpty().mapToViewEntityList().filter { it.isSelected }
                _currentBudgetTransactions.value = period.budgetTransactions.orEmpty().mapToViewEntityList()
                _performDashboardUiEvent.call(DashboardUiEvent.DisplayLoadingState(LoadingState.Success, IntLoadingType.SWIPE_TO_REFRESH))
*//*

            }
*/
    }
}