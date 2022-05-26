package com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgetransactioncreateedit.subscreens.categoryselection

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.s95ammar.budgetplanner.models.repository.CategoryOfPeriodRepository
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgetransactioncreateedit.data.CategoryOfPeriodSimple
import com.s95ammar.budgetplanner.ui.common.LoadingState
import com.s95ammar.budgetplanner.util.INVALID
import com.s95ammar.budgetplanner.util.lifecycleutil.EventMutableLiveData
import com.s95ammar.budgetplanner.util.lifecycleutil.LoaderMutableLiveData
import com.s95ammar.budgetplanner.util.lifecycleutil.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgetransactioncreateedit.BudgetTransactionCreateEditFragmentArgs as FragmentArgs
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgetransactioncreateedit.subscreens.categoryselection.data.BudgetTransactionCategorySelectionUiEvent as UiEvent

@HiltViewModel
class BudgetTransactionCategorySelectionViewModel @Inject constructor(
    private val repository: CategoryOfPeriodRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val periodId = savedStateHandle.get<Int>(FragmentArgs::periodId.name) ?: Int.INVALID

    private val _categoriesOfPeriod = LoaderMutableLiveData<List<CategoryOfPeriodSimple>> { loadCategoriesOfPeriod() }
    private val _performUiEvent = EventMutableLiveData<UiEvent>()

    val categoriesOfPeriod = _categoriesOfPeriod.asLiveData()
    val performUiEvent = _performUiEvent.asEventLiveData()

    fun onCategoryOfPeriodItemClick(position: Int) {
        _categoriesOfPeriod.value?.getOrNull(position)?.let { categoryOfPeriod ->
            _performUiEvent.call(UiEvent.SetResult(categoryOfPeriod))
            _performUiEvent.call(UiEvent.Exit)
        }
    }

    private fun loadCategoriesOfPeriod() {
        viewModelScope.launch {
            repository.getCategoryOfPeriodSimple(periodId)
                .onStart {
                    _performUiEvent.call(UiEvent.DisplayLoadingState(LoadingState.Loading))
                }
                .catch {
                    _performUiEvent.call(UiEvent.DisplayLoadingState(LoadingState.Error(it)))
                }
                .collect { categoryOfPeriodSimple ->
                    _categoriesOfPeriod.value = categoryOfPeriodSimple.mapNotNull { entity ->
                        CategoryOfPeriodSimple.Mapper.fromEntity(entity)
                    }
                    _performUiEvent.call(UiEvent.DisplayLoadingState(LoadingState.Success))
                }
        }
    }
}