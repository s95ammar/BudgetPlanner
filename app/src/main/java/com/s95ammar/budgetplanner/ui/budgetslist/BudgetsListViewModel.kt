package com.s95ammar.budgetplanner.ui.budgetslist

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.s95ammar.budgetplanner.Logger
import com.s95ammar.budgetplanner.models.data.Budget
import com.s95ammar.budgetplanner.models.repository.Repository
import com.s95ammar.budgetplanner.ui.budgetslist.entity.BudgetViewEntity
import com.s95ammar.budgetplanner.util.EventMutableLiveData
import kotlinx.coroutines.launch
import kotlin.system.measureNanoTime
import kotlin.system.measureTimeMillis

class BudgetsListViewModel @ViewModelInject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _navigateToEditBudget = EventMutableLiveData<Int>()

    val allBudgets by lazy { getAllBudgetsViewEntities() }
    val navigateToEditBudget = _navigateToEditBudget.asEventLiveData()

    private fun getAllBudgetsViewEntities() = repository.getAllBudgets().map { budgetList ->
        budgetList.map {
            BudgetViewEntity(
                id = it.id,
                name = it.name,
                totalBalance = it.totalBalance,
                totalSpendingEstimate = 0, // TODO
                totalSavings = 0 // TODO
            )
        }
    }

    fun onBudgetItemClick(position: Int) {
        allBudgets.value?.getOrNull(position)?.let { budget ->
            _navigateToEditBudget.call(budget.id)
        }
    }
}