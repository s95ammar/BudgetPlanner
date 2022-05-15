package com.s95ammar.budgetplanner.ui.appscreens.dashboard.pager.budgettransactions

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.s95ammar.budgetplanner.models.repository.BudgetTransactionRepository
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.common.data.BudgetTransaction
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.pager.budgettransactions.adapter.BudgetTransactionsItemType
import com.s95ammar.budgetplanner.ui.common.LoadingState
import com.s95ammar.budgetplanner.util.INVALID
import com.s95ammar.budgetplanner.util.lifecycleutil.EventMutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.pager.budgettransactions.data.BudgetTransactionsUiEvent as UiEvent

@HiltViewModel
class BudgetTransactionsViewModel @Inject constructor(
    private val repository: BudgetTransactionRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _budgetTransactions = MutableLiveData<List<BudgetTransaction>>()
    private val _performUiEvent = EventMutableLiveData<UiEvent>()

    val budgetTransactionItems = _budgetTransactions.map { list ->
        buildList {
            addAll(list.map { BudgetTransactionsItemType.ListItem(it) })
            if (list.any { it.latLng != null }) {
                add(BudgetTransactionsItemType.ViewOnMap(list.first().periodId))
            }
        }
    }
    val performUiEvent = _performUiEvent.asEventLiveData()

    fun onPeriodChanged(periodId: Int) {
        loadBudgetTransactions(periodId)
    }

    fun onBudgetTransactionClick(budgetTransaction: BudgetTransaction) {
        _performUiEvent.call(UiEvent.NavigateToEditBudgetTransaction(budgetTransaction.periodId, budgetTransaction.id))
    }

    fun onBudgetTransactionLongClick(budgetTransaction: BudgetTransaction) {
        _performUiEvent.call(UiEvent.ShowBottomSheet(budgetTransaction))
    }

    fun deleteBudgetTransaction(id: Int) = viewModelScope.launch {
        repository.deleteBudgetTransactionFlow(id)
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

    fun onShowOnMap() {
        _performUiEvent.call(UiEvent.ShowOnMap)
    }

    private fun loadBudgetTransactions(periodId: Int) = viewModelScope.launch {
        if (periodId == Int.INVALID) return@launch

        repository.getPeriodBudgetTransactionsFlow(periodId)
            .onStart {
                _performUiEvent.call(UiEvent.DisplayLoadingState(LoadingState.Loading))
            }
            .catch { throwable ->
                _performUiEvent.call(UiEvent.DisplayLoadingState(LoadingState.Error(throwable)))
            }
            .collect { budgetTransactionJoinEntityList ->
                _performUiEvent.call(UiEvent.DisplayLoadingState(LoadingState.Success))
                _budgetTransactions.value = budgetTransactionJoinEntityList.mapNotNull(BudgetTransaction.Mapper::fromEntity)
                    .sortedBy { it.creationUnixMs }
            }
    }

}