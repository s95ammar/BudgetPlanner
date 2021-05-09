package com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgetransactioncreateedit.subscreens.categoryselection

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.s95ammar.budgetplanner.models.repository.PeriodicCategoryRepository
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgetransactioncreateedit.BudgetTransactionCreateEditFragmentArgs
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgetransactioncreateedit.subscreens.categoryselection.data.PeriodicCategoryIdAndName
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
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgetransactioncreateedit.subscreens.categoryselection.data.BudgetTransactionCategorySelectionUiEvent as UiEvent

@HiltViewModel
class BudgetTransactionCategorySelectionViewModel @Inject constructor(
    private val repository: PeriodicCategoryRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val periodId = savedStateHandle.get<Int>(BudgetTransactionCreateEditFragmentArgs::periodId.name) ?: Int.INVALID

    private val _periodicCategories = LoaderMutableLiveData<List<PeriodicCategoryIdAndName>> { loadPeriodicCategories() }
    private val _performUiEvent = EventMutableLiveData<UiEvent>()

    val periodicCategories = _periodicCategories.asLiveData()
    val performUiEvent = _performUiEvent.asEventLiveData()

    fun onPeriodicCategoryItemClick(position: Int) {
        _periodicCategories.value?.getOrNull(position)?.let { periodicCategory ->
            _performUiEvent.call(UiEvent.SetResult(periodicCategory))
            _performUiEvent.call(UiEvent.Exit)
        }
    }

    private fun loadPeriodicCategories() {
        viewModelScope.launch {
            repository.getPeriodicCategoryIdAndNameListFlow(periodId)
                .onStart {
                    _performUiEvent.call(UiEvent.DisplayLoadingState(LoadingState.Loading))
                }
                .catch {
                    _performUiEvent.call(UiEvent.DisplayLoadingState(LoadingState.Error(it)))
                }
                .collect { PeriodicCategoryIdAndNameJoinEntityList ->
                    _periodicCategories.value = PeriodicCategoryIdAndNameJoinEntityList.mapNotNull { entity ->
                        PeriodicCategoryIdAndName.Mapper.fromEntity(entity)
                    }
                    _performUiEvent.call(UiEvent.DisplayLoadingState(LoadingState.Success))
                }
        }
    }
}