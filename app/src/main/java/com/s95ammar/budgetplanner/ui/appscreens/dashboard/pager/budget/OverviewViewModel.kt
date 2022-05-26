package com.s95ammar.budgetplanner.ui.appscreens.dashboard.pager.budget

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.s95ammar.budgetplanner.models.repository.CategoryOfPeriodRepository
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.common.data.CategoryOfPeriod
import com.s95ammar.budgetplanner.ui.common.LoadingState
import com.s95ammar.budgetplanner.util.INVALID
import com.s95ammar.budgetplanner.util.lifecycleutil.EventMutableLiveData
import com.s95ammar.budgetplanner.util.lifecycleutil.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.pager.budget.data.BudgetUiEvent as UiEvent

@HiltViewModel
class OverviewViewModel @Inject constructor(
    private val repository: CategoryOfPeriodRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _categoriesOfPeriod = MutableLiveData<List<CategoryOfPeriod>>()
    private val _performUiEvent = EventMutableLiveData<UiEvent>()

    val categoriesOfPeriod = _categoriesOfPeriod.asLiveData()
    val performUiEvent = _performUiEvent.asEventLiveData()

    fun onPeriodChanged(periodId: Int) {
        loadCategoriesOfPeriod(periodId)
    }

    private fun loadCategoriesOfPeriod(periodId: Int) = viewModelScope.launch {
        if (periodId == Int.INVALID) return@launch

        repository.getCategoriesOfPeriodFlow(periodId)
            .onStart {
                _performUiEvent.call(UiEvent.DisplayLoadingState(LoadingState.Loading))
            }
            .catch { throwable ->
                _performUiEvent.call(UiEvent.DisplayLoadingState(LoadingState.Error(throwable)))
            }
            .collect { categoryOfPeriodJoinEntityList ->
                _performUiEvent.call(UiEvent.DisplayLoadingState(LoadingState.Success))
                _categoriesOfPeriod.value = categoryOfPeriodJoinEntityList.mapNotNull(CategoryOfPeriod.JoinEntityMapper::fromEntity)
            }

    }
}