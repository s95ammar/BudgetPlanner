package com.s95ammar.budgetplanner.ui.appscreens.dashboard.expenses

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.s95ammar.budgetplanner.models.repository.LocalRepository
import com.s95ammar.budgetplanner.models.repository.RemoteRepository
import kotlinx.coroutines.launch

class ExpensesViewModel @ViewModelInject constructor(
    private val localRepository: LocalRepository,
    private val remoteRepository: RemoteRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    fun onPeriodChanged(periodId: Int) {
        loadPeriodBudgetTransactions(periodId)
    }

    private fun loadPeriodBudgetTransactions(periodId: Int) {
        viewModelScope.launch {
            remoteRepository.getBudgetTransactionsForPeriod(periodId)
                .onSuccess {

                }
                .onError {  }
        }
    }

}