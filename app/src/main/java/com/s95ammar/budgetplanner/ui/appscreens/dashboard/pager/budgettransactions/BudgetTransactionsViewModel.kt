package com.s95ammar.budgetplanner.ui.appscreens.dashboard.pager.budgettransactions

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.s95ammar.budgetplanner.models.repository.BudgetTransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BudgetTransactionsViewModel @Inject constructor(
    private val repository: BudgetTransactionRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

}