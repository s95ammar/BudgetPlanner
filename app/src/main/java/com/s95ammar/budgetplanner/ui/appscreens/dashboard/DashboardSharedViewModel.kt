package com.s95ammar.budgetplanner.ui.appscreens.dashboard

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.viewModelScope
import com.s95ammar.budgetplanner.models.repository.CategoryOfPeriodRepository
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.common.data.CategoryOfPeriod
import com.s95ammar.budgetplanner.ui.common.LoadingState
import com.s95ammar.budgetplanner.util.INVALID
import com.s95ammar.budgetplanner.util.lifecycleutil.EventMutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.data.DashboardUiEvent as UiEvent

@HiltViewModel
class DashboardSharedViewModel @Inject constructor(
    private val categoryOfPeriodRepository: CategoryOfPeriodRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _selectedPeriodId = MutableLiveData(Int.INVALID)
    private val _performUiEvent = EventMutableLiveData<UiEvent>()

    val selectedPeriodId = _selectedPeriodId.distinctUntilChanged()
    val performUiEvent = _performUiEvent.asEventLiveData()

    fun onPeriodChanged(periodId: Int?) {
        _selectedPeriodId.value = periodId ?: Int.INVALID
    }

    fun onNavigateToEditBudgetTransaction(periodId: Int, budgetTransactionId: Int) {
        _performUiEvent.call(UiEvent.NavigateToEditBudgetTransaction(periodId, budgetTransactionId))
    }

    fun onNavigateToBudgetTransactionsMap() {
        _selectedPeriodId.value?.let { periodId ->
            _performUiEvent.call(UiEvent.NavigateToBudgetTransactionsMap(periodId))
        }
    }

    fun onCreateEditEstimate(categoryOfPeriod: CategoryOfPeriod) {
        _performUiEvent.call(UiEvent.NavigateToCreateEditEstimate(categoryOfPeriod))
    }

    fun onCategoryOfPeriodEstimateChanged(categoryOfPeriod: CategoryOfPeriod, estimateOrNull: Double?) = viewModelScope.launch {
        if (categoryOfPeriod.estimate == estimateOrNull) return@launch

        CategoryOfPeriod.EntityMapper.toEntity(categoryOfPeriod.copy(estimate = estimateOrNull))?.let { entity ->
            categoryOfPeriodRepository.updateCategoryOfPeriod(entity)
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
    }

}
