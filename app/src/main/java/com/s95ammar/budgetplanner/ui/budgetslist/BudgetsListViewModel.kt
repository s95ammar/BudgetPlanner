package com.s95ammar.budgetplanner.ui.budgetslist

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.s95ammar.budgetplanner.models.repository.Repository
import com.s95ammar.budgetplanner.ui.budgetslist.entity.BudgetViewEntity

class BudgetsListViewModel @ViewModelInject constructor(
    private val repository: Repository
) : ViewModel() {

    fun getAllBudgets() = repository.getAllBudgets().map {  budgetList ->
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

    fun onBudgetItemClick(id: Int) {

    }
}