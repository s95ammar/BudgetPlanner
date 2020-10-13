package com.s95ammar.budgetplanner.ui.appscreens.budgetslist

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.s95ammar.budgetplanner.models.Resource
import com.s95ammar.budgetplanner.models.Result
import com.s95ammar.budgetplanner.models.ResultStateListener
import com.s95ammar.budgetplanner.models.data.Budget
import com.s95ammar.budgetplanner.models.repository.LocalRepository
import com.s95ammar.budgetplanner.ui.appscreens.budgetslist.data.BudgetViewEntity
import com.s95ammar.budgetplanner.util.NO_ITEM
import com.s95ammar.budgetplanner.util.lifecycleutil.EventMutableLiveData
import com.s95ammar.budgetplanner.util.lifecycleutil.asLiveData
import kotlinx.coroutines.launch

class BudgetsListViewModel @ViewModelInject constructor(private val localRepository: LocalRepository) : ViewModel() {

    private val _allBudgets = MediatorLiveData<Resource<List<BudgetViewEntity>>>().apply {
        addSource(getAllBudgetsViewEntities()) { value = it }
    }
    private val _navigateToEditBudget = EventMutableLiveData<Int>()
    private val _showBottomSheet = EventMutableLiveData<BudgetViewEntity>()
    private val _displayDeleteResultState = EventMutableLiveData<Result>()

    val allBudgets = _allBudgets.asLiveData()
    val navigateToEditBudget = _navigateToEditBudget.asEventLiveData()
    val showBottomSheet = _showBottomSheet.asEventLiveData()
    val displayDeleteResultState = _displayDeleteResultState.asEventLiveData()


    fun onBudgetItemClick(position: Int) {
        _allBudgets.list?.getOrNull(position)?.let { budget ->
            _navigateToEditBudget.call(budget.id)
        }
    }

    fun onBudgetItemLongClick(position: Int) {
        _allBudgets.list?.getOrNull(position)?.let { budget ->
            _showBottomSheet.call(budget)
        }
    }

    fun onActiveBudgetChanged(id: Int) {
        _allBudgets.list?.map { it.copy(isActive = (it.id == id)) }?.let { budgetsList ->
            _allBudgets.value = Resource.Success(budgetsList.sortedByActiveThenById())
        }
    }

    fun saveAndDisplayNewActiveBudget(id: Int) {
        localRepository.saveActiveBudgetId(id)
        onActiveBudgetChanged(id)
    }

    fun onDeleteBudget(id: Int) = viewModelScope.launch {
        deleteBudget(budgetId = id, listener = object : ResultStateListener<Unit> {

            override fun onLoading() = _displayDeleteResultState.call(Result.Loading)

            override fun onSuccess(data: Unit?) {
                if (id == localRepository.loadActiveBudgetId())
                    localRepository.saveActiveBudgetId(Int.NO_ITEM)
                _displayDeleteResultState.call(Result.Success)
            }

            override fun onError(throwable: Throwable) = _displayDeleteResultState.call(Result.Error(throwable))

        })
    }

    private fun deleteBudget(budgetId: Int, listener: ResultStateListener<Unit>)  = viewModelScope.launch {
        try {
            listener.onLoading()
            val budget = localRepository.getBudgetById(budgetId)
            localRepository.deleteBudget(budget)
            listener.onSuccess()
        } catch (e: Exception) {
            listener.onError(e)
        }
    }

    private fun getAllBudgetsViewEntities() = liveData {
        emit(Resource.Loading())
        try {
            emitSource(localRepository.getAllBudgetsLiveData().map { Resource.Success(it.toSortedViewEntitiesList()) })
        } catch (e: Exception) {
            emit(Resource.Error<List<BudgetViewEntity>>(e))
        }
    }

    private fun List<Budget>.toSortedViewEntitiesList(): List<BudgetViewEntity> {
        val activeBudgetId = localRepository.loadActiveBudgetId()

        return map { budget ->
            BudgetViewEntity(
                id = budget.id,
                name = budget.name,
                isActive = budget.id == activeBudgetId,
                totalBalance = budget.totalBalance,
                totalSpendingEstimate = 0, // TODO
                totalSavings = 0 // TODO
            )
        }.sortedByActiveThenById()
    }

    private val LiveData<Resource<List<BudgetViewEntity>>>.list: List<BudgetViewEntity>?
        get() = (_allBudgets.value as? Resource.Success)?.data

    private fun List<BudgetViewEntity>.sortedByActiveThenById() = sortedWith(compareByDescending<BudgetViewEntity>{ it.isActive }.thenBy { it.id })
}