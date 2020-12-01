package com.s95ammar.budgetplanner.ui.appscreens.dashboard.transactions

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.s95ammar.budgetplanner.models.mappers.BudgetTransactionApiViewMapper
import com.s95ammar.budgetplanner.models.repository.LocalRepository
import com.s95ammar.budgetplanner.models.repository.RemoteRepository
import com.s95ammar.budgetplanner.models.view.BudgetTransactionViewEntity
import com.s95ammar.budgetplanner.ui.appscreens.auth.common.LoadingState
import com.s95ammar.budgetplanner.util.lifecycleutil.EventMutableLiveData
import com.s95ammar.budgetplanner.util.lifecycleutil.asLiveData
import kotlinx.coroutines.launch

class TransactionsViewModel @ViewModelInject constructor(
    private val localRepository: LocalRepository,
    private val remoteRepository: RemoteRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _transactions = MutableLiveData<List<BudgetTransactionViewEntity>>()
    private val _displayLoadingState = EventMutableLiveData<LoadingState>(LoadingState.Cold)

    val transactions = _transactions.asLiveData()
    val displayLoadingState = _displayLoadingState.asEventLiveData()

    fun onPeriodChanged(periodId: Int) {
        loadPeriodBudgetTransactions(periodId)
    }

    private fun loadPeriodBudgetTransactions(periodId: Int) {
        _displayLoadingState.call(LoadingState.Loading)
        viewModelScope.launch {
            remoteRepository.getBudgetTransactionsForPeriod(periodId)
                .onSuccess { budgetTransactionsApiEntities ->
                    _displayLoadingState.call(LoadingState.Success)
                    _transactions.value = budgetTransactionsApiEntities.orEmpty().mapNotNull {
                        BudgetTransactionApiViewMapper.toViewEntity(it)
                    }

                }
                .onError { _displayLoadingState.call(LoadingState.Error(it)) }


        }
    }

}