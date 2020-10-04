package com.s95ammar.budgetplanner.ui.budgetslist

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.s95ammar.budgetplanner.models.Resource
import com.s95ammar.budgetplanner.models.data.Budget
import com.s95ammar.budgetplanner.models.repository.Repository
import com.s95ammar.budgetplanner.ui.budgetslist.entity.BudgetViewEntity
import com.s95ammar.budgetplanner.util.EventMutableLiveData

class BudgetsListViewModel @ViewModelInject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _navigateToEditBudget = EventMutableLiveData<Int>()

    val allBudgets by lazy { getAllBudgetsViewEntities() }
    val navigateToEditBudget = _navigateToEditBudget.asEventLiveData()

    private fun getAllBudgetsViewEntities() = liveData {
        emit(Resource.Loading())
        try {
            emitSource(repository.getAllBudgets().map { Resource.Success(it.toViewEntitiesList()) })
        } catch (e: Exception) {
            emit(Resource.Error<List<BudgetViewEntity>>(e))
        }
    }

    private fun List<Budget>.toViewEntitiesList() = map { budget ->
        BudgetViewEntity(
            id = budget.id,
            name = budget.name,
            totalBalance = budget.totalBalance,
            totalSpendingEstimate = 0, // TODO
            totalSavings = 0 // TODO
        )
    }

    fun onBudgetItemClick(position: Int) {
        (allBudgets.value as? Resource.Success)?.data?.getOrNull(position)?.let { budget ->
            _navigateToEditBudget.call(budget.id)
        }
    }
}