package com.s95ammar.budgetplanner.ui.budgetslist

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.s95ammar.budgetplanner.models.Resource
import com.s95ammar.budgetplanner.models.data.Budget
import com.s95ammar.budgetplanner.models.repository.Repository
import com.s95ammar.budgetplanner.ui.base.StorageViewModel
import com.s95ammar.budgetplanner.ui.budgetslist.entity.BudgetViewEntity
import com.s95ammar.budgetplanner.util.lifecycleutil.EventMutableLiveData
import com.s95ammar.budgetplanner.util.lifecycleutil.asLiveData
import dagger.hilt.android.qualifiers.ApplicationContext

class BudgetsListViewModel @ViewModelInject constructor(
    @ApplicationContext context: Context,
    private val repository: Repository
) : StorageViewModel(context) {

    // TODO: implement injecting storage source as interface type & move implementation functions out of view model

    private var activeBudgetId = loadActiveBudgetId()


    private val _allBudgets = MediatorLiveData<Resource<List<BudgetViewEntity>>>().apply {
        addSource(getAllBudgetsViewEntities()) { value = it }
    }
    private val _navigateToEditBudget = EventMutableLiveData<Int>()

    val allBudgets = _allBudgets.asLiveData()
    val navigateToEditBudget = _navigateToEditBudget.asEventLiveData()

    private fun getAllBudgetsViewEntities() = liveData {
        emit(Resource.Loading())
        try {
            emitSource(repository.getAllBudgets().map { Resource.Success(it.toViewEntitiesList()) })
        } catch (e: Exception) {
            emit(Resource.Error<List<BudgetViewEntity>>(e))
        }
    }

    fun onBudgetItemClick(position: Int) {
        _allBudgets.list?.getOrNull(position)?.let { budget ->
            _navigateToEditBudget.call(budget.id)
        }
    }

    fun onActiveBudgetChanged(id: Int) {
        activeBudgetId = id
        _allBudgets.list?.map { it.copy(isActive = (it.id == activeBudgetId)) }?.let { budgetsList ->
            _allBudgets.value = Resource.Success(budgetsList)
        }
    }

    private fun List<Budget>.toViewEntitiesList() = map { budget ->
        BudgetViewEntity(
            id = budget.id,
            name = budget.name,
            isActive = budget.id == activeBudgetId,
            totalBalance = budget.totalBalance,
            totalSpendingEstimate = 0, // TODO
            totalSavings = 0 // TODO
        )
    }

    private val LiveData<Resource<List<BudgetViewEntity>>>.list: List<BudgetViewEntity>?
        get() = (_allBudgets.value as? Resource.Success)?.data
}