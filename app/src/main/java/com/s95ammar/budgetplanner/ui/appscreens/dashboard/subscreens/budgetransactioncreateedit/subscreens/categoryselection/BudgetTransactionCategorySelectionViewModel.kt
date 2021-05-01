package com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgetransactioncreateedit.subscreens.categoryselection

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.s95ammar.budgetplanner.models.repository.CategoriesRepository
import com.s95ammar.budgetplanner.ui.appscreens.categories.common.data.Category
import com.s95ammar.budgetplanner.ui.common.LoadingState
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
    private val repository: CategoriesRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _categories = LoaderMutableLiveData<List<Category>> { loadCategories() }
    private val _performUiEvent = EventMutableLiveData<UiEvent>()

    val categories = _categories.asLiveData()
    val performUiEvent = _performUiEvent.asEventLiveData()


    fun onCategoryItemClick(position: Int) {
        _categories.value?.getOrNull(position)?.let { category ->
            _performUiEvent.call(UiEvent.SetResult(category))
            _performUiEvent.call(UiEvent.Exit)
        }
    }

    private fun loadCategories() {
        viewModelScope.launch {
            repository.getAllUserCategoriesFlow()
                .onStart {
                    _performUiEvent.call(UiEvent.DisplayLoadingState(LoadingState.Loading))
                }
                .catch {
                    _performUiEvent.call(UiEvent.DisplayLoadingState(LoadingState.Error(it)))
                }
                .collect { categoryEntityList ->
/*
                    val categories = categoryEntityList.mapNotNull { entity -> CategoryViewEntity.EntityMapper.toViewEntity(entity) }
                    _categories.value = categories
                    _performUiEvent.call(UiEvent.DisplayLoadingState(LoadingState.Success))
*/
                }
        }
    }
}