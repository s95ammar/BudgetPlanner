package com.s95ammar.budgetplanner.ui.appscreens.categories

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.s95ammar.budgetplanner.models.repository.CategoriesRepository
import com.s95ammar.budgetplanner.ui.appscreens.auth.common.LoadingState
import com.s95ammar.budgetplanner.ui.appscreens.categories.common.data.CategoryViewEntity
import com.s95ammar.budgetplanner.ui.appscreens.categories.data.CategoriesUiEvent
import com.s95ammar.budgetplanner.util.NO_ITEM
import com.s95ammar.budgetplanner.util.lifecycleutil.EventMutableLiveData
import com.s95ammar.budgetplanner.util.lifecycleutil.LoaderMutableLiveData
import com.s95ammar.budgetplanner.util.lifecycleutil.asLiveData
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CategoriesViewModel @ViewModelInject constructor(
    private val repository: CategoriesRepository
) : ViewModel() {

    private val _allCategories = LoaderMutableLiveData<List<CategoryViewEntity>> { loadAllCategories() }
    private val _performUiEvent = EventMutableLiveData<CategoriesUiEvent>()

    val allCategories = _allCategories.asLiveData()
    val performUiEvent = _performUiEvent.asEventLiveData()

    fun refresh() {
        loadAllCategories()
    }

    fun onNavigateToCreateCategory() {
        _performUiEvent.call(CategoriesUiEvent.OnNavigateToEditCategory(Int.NO_ITEM))
    }

    fun onCategoryItemClick(position: Int) {
        _allCategories.value?.getOrNull(position)?.let { category ->
            _performUiEvent.call(CategoriesUiEvent.OnNavigateToEditCategory(category.id))
        }
    }

    fun onCategoryItemLongClick(position: Int) {
        _allCategories.value?.getOrNull(position)?.let { category ->
            _performUiEvent.call(CategoriesUiEvent.ShowBottomSheet(category))
        }
    }

    fun deleteCategory(id: Int) = viewModelScope.launch {
        _performUiEvent.call(CategoriesUiEvent.DisplayLoadingState(LoadingState.Loading))
        repository.deleteCategory(id)
            .catch { _performUiEvent.call(CategoriesUiEvent.DisplayLoadingState(LoadingState.Error(it))) }
            .collect { refresh() }
    }

    private fun loadAllCategories() {
        viewModelScope.launch {
            _performUiEvent.call(CategoriesUiEvent.DisplayLoadingState(LoadingState.Loading))
            repository.getAllUserCategories()
                .catch { _performUiEvent.call(CategoriesUiEvent.DisplayLoadingState(LoadingState.Error(it))) }
                .collect { categoryApiEntities ->
                    val categories = categoryApiEntities.mapNotNull { apiEntity -> CategoryViewEntity.ApiMapper.toViewEntity(apiEntity) }
                    _allCategories.value = categories
                    _performUiEvent.call(CategoriesUiEvent.DisplayLoadingState(LoadingState.Success))
                }
        }
    }

}