package com.s95ammar.budgetplanner.ui.appscreens.dashboard.pager.budget

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.s95ammar.budgetplanner.models.repository.PeriodicCategoryRepository
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.common.data.PeriodicCategory
import com.s95ammar.budgetplanner.util.INVALID
import com.s95ammar.budgetplanner.util.lifecycleutil.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BudgetViewModel @Inject constructor(
    private val repository: PeriodicCategoryRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _periodicCategories = MutableLiveData<List<PeriodicCategory>>()

    val periodicCategories = _periodicCategories.asLiveData()

    fun onPeriodChanged(periodId: Int) {
        loadPeriodicCategories(periodId)
    }

    private fun loadPeriodicCategories(periodId: Int) = viewModelScope.launch {
        if (periodId == Int.INVALID) return@launch

        repository.getPeriodicCategoriesFlow(periodId)
            .onStart {
                // TODO: loading
            }
            .catch {
                // TODO: loading
            }
            .collect { periodicCategoryJoinEntityList ->
                _periodicCategories.value = periodicCategoryJoinEntityList.mapNotNull(PeriodicCategory.JoinEntityMapper::fromEntity)
            }

    }
}