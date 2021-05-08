package com.s95ammar.budgetplanner.ui.appscreens.dashboard.pager.budgettransactions

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.s95ammar.budgetplanner.models.repository.BudgetTransactionRepository
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.common.data.BudgetTransaction
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.pager.budget.data.BudgetUiEvent
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

@HiltViewModel
class BudgetTransactionsViewModel @Inject constructor(
    private val repository: BudgetTransactionRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _budgetTransactions = MutableLiveData<List<BudgetTransaction>>()
    private val _performUiEvent = EventMutableLiveData<BudgetUiEvent>()

    val budgetTransactions = _budgetTransactions.asLiveData()
    val performUiEvent = _performUiEvent.asEventLiveData()

    fun onPeriodChanged(periodId: Int) {
        loadBudgetTransactions(periodId)
    }

    private fun loadBudgetTransactions(periodId: Int) = viewModelScope.launch {
        if (periodId == Int.INVALID) return@launch

        repository.getBudgetTransactionsFlow(periodId)
            .onStart {
                _performUiEvent.call(BudgetUiEvent.DisplayLoadingState(LoadingState.Loading))
            }
            .catch { throwable ->
                _performUiEvent.call(BudgetUiEvent.DisplayLoadingState(LoadingState.Error(throwable)))
            }
            .collect { budgetTransactionJoinEntityList ->
                _performUiEvent.call(BudgetUiEvent.DisplayLoadingState(LoadingState.Success))
                _budgetTransactions.value = budgetTransactionJoinEntityList.mapNotNull(BudgetTransaction.Mapper::fromEntity)
            }
    }

}