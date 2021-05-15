package com.s95ammar.budgetplanner.ui.appscreens.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.s95ammar.budgetplanner.models.repository.CategoriesRepository
import com.s95ammar.budgetplanner.ui.appscreens.categories.common.data.Category
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
import com.s95ammar.budgetplanner.ui.appscreens.categories.data.CategoriesUiEvent as UiEvent

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val repository: CategoriesRepository
) : ViewModel() {

    private val _allCategories = LoaderMutableLiveData<List<Category>> { loadAllCategories() }
    private val _performUiEvent = EventMutableLiveData<UiEvent>()

    val allCategories = _allCategories.asLiveData()
    val performUiEvent = _performUiEvent.asEventLiveData()

    fun onNavigateToCreateCategory() {
        _performUiEvent.call(UiEvent.ListenAndNavigateToEditCategory(Int.INVALID))
    }

    fun onCategoryItemClick(position: Int) {
        _allCategories.value?.getOrNull(position)?.let { category ->
            _performUiEvent.call(UiEvent.ListenAndNavigateToEditCategory(category.id))
        }
    }

    fun onCategoryItemLongClick(position: Int) {
        _allCategories.value?.getOrNull(position)?.let { category ->
            _performUiEvent.call(UiEvent.ShowBottomSheet(category))
        }
    }

    fun deleteCategory(id: Int) = viewModelScope.launch {
        repository.deleteCategoryFlow(id)
            .onStart {
                _performUiEvent.call(UiEvent.DisplayLoadingState(LoadingState.Loading))
            }
            .catch {
                _performUiEvent.call(UiEvent.DisplayLoadingState(LoadingState.Error(it)))
            }
            .collect {
                _performUiEvent.call(UiEvent.DisplayLoadingState(LoadingState.Success))
            }
    }

    private fun loadAllCategories() {
        viewModelScope.launch {
            repository.getAllUserCategoriesFlow()
                .onStart {
                    _performUiEvent.call(UiEvent.DisplayLoadingState(LoadingState.Loading))
                }
                .catch {
                    _performUiEvent.call(UiEvent.DisplayLoadingState(LoadingState.Error(it)))
                }
                .collect { categoryEntityList ->
                    _allCategories.value = categoryEntityList.mapNotNull(Category.Mapper::fromEntity)
                    _performUiEvent.call(UiEvent.DisplayLoadingState(LoadingState.Success))
                }
        }
    }

}