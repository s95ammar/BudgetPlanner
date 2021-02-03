package com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budget

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.s95ammar.budgetplanner.models.repository.LocalRepository
import com.s95ammar.budgetplanner.models.repository.RemoteRepository
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.common.data.PeriodicCategoryViewEntity
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budget.data.BudgetUiEvent
import com.s95ammar.budgetplanner.util.lifecycleutil.EventMutableLiveData
import com.s95ammar.budgetplanner.util.lifecycleutil.asLiveData

class BudgetViewModel @ViewModelInject constructor(
    private val localRepository: LocalRepository,
    private val remoteRepository: RemoteRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _cachedData = MutableLiveData(mutableMapOf<Int, List<PeriodicCategoryViewEntity>>())

    private val _currentPeriodId = MutableLiveData<Int>()

    private val _resultPeriodicCategories = MediatorLiveData<List<PeriodicCategoryViewEntity>>().apply {
        addSource(_cachedData) { map ->

            _currentPeriodId.value?.let { periodId ->
                value = map[periodId].orEmpty().filter { it.isSelected }
            }
        }
    }
    private val _performUiEvent = EventMutableLiveData<BudgetUiEvent>()

    val currentPeriodId = _currentPeriodId.asLiveData()
    val resultPeriodicCategories = _resultPeriodicCategories.asLiveData()
    val performUiEvent = _performUiEvent.asEventLiveData()

    fun onSelectedPeriodChanged(periodId: Int) {
        if (_currentPeriodId.value != periodId) {

            _currentPeriodId.value = periodId
            val cachedPeriodicCategories = _cachedData.value?.get(periodId)
            if (cachedPeriodicCategories != null) {
                _resultPeriodicCategories.value = cachedPeriodicCategories.filter { it.isSelected }
            } else {
                _performUiEvent.call(BudgetUiEvent.OnNoLocalData)
            }

        }
    }

    fun onPeriodicCategoriesLoaded(periodId: Int, periodicCategories: List<PeriodicCategoryViewEntity>) {
        _cachedData.value = _cachedData.value?.apply { set(periodId, periodicCategories) }
    }

    fun onEditPeriod() {
        _currentPeriodId.value?.let { _performUiEvent.call(BudgetUiEvent.OnEditPeriod(it)) }
    }

}