package com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.transactions

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.s95ammar.budgetplanner.models.repository.BudgetTransactionRepository
import com.s95ammar.budgetplanner.ui.appscreens.auth.common.LoadingState
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.common.data.BudgetTransactionViewEntity
import com.s95ammar.budgetplanner.util.lifecycleutil.EventMutableLiveData
import com.s95ammar.budgetplanner.util.lifecycleutil.asLiveData
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class TransactionsViewModel @ViewModelInject constructor(
    private val repository: BudgetTransactionRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _transactions = MutableLiveData<List<BudgetTransactionViewEntity>>()
    private val _displayLoadingState = EventMutableLiveData<LoadingState>(LoadingState.Cold) // TODO: Change by creating UiEvent

    val transactions = _transactions.asLiveData()
    val displayLoadingState = _displayLoadingState.asEventLiveData()

    fun onPeriodChanged(periodId: Int) {
        loadPeriodBudgetTransactions(periodId)
    }

    private fun loadPeriodBudgetTransactions(periodId: Int) {
        _displayLoadingState.call(LoadingState.Loading)
        viewModelScope.launch {
            repository.getBudgetTransactionsForPeriod(periodId)
                .catch { _displayLoadingState.call(LoadingState.Error(it)) }
                .collect { budgetTransactionsApiEntities ->
                    _displayLoadingState.call(LoadingState.Success)
                    _transactions.value = budgetTransactionsApiEntities.mapNotNull {
                        BudgetTransactionViewEntity.ApiMapper.toViewEntity(it)
                    }

                }


        }
    }

}