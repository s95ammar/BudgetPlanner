package com.s95ammar.budgetplanner.ui.appscreens.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.s95ammar.budgetplanner.models.repository.CategoriesRepository
import com.s95ammar.budgetplanner.ui.appscreens.categories.common.data.Category
import com.s95ammar.budgetplanner.ui.appscreens.categories.data.CategoriesUiEvent
import com.s95ammar.budgetplanner.util.NO_ITEM
import com.s95ammar.budgetplanner.util.lifecycleutil.EventMutableLiveData
import com.s95ammar.budgetplanner.util.lifecycleutil.LoaderMutableLiveData
import com.s95ammar.budgetplanner.util.lifecycleutil.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val repository: CategoriesRepository
) : ViewModel() {

    private val _allCategories = LoaderMutableLiveData<List<Category>> { loadAllCategories() }
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

    fun deleteCategory(id: Int) = viewModelScope.launch {/*
        repository.deleteCategory(id)
            .onStart {
                _performUiEvent.call(CategoriesUiEvent.DisplayLoadingState(LoadingState.Loading))
            }
            .catch {
                _performUiEvent.call(CategoriesUiEvent.DisplayLoadingState(LoadingState.Error(it)))
            }
            .collect {
                _performUiEvent.call(CategoriesUiEvent.DisplayLoadingState(LoadingState.Success))
                refresh()
            }
    */}

    private fun loadAllCategories() {/*
        viewModelScope.launch {
            repository.getAllUserCategories()
                .onStart {
                    _performUiEvent.call(CategoriesUiEvent.DisplayLoadingState(LoadingState.Loading))
                }
                .catch {
                    _performUiEvent.call(CategoriesUiEvent.DisplayLoadingState(LoadingState.Error(it)))
                }
                .collect { categoryApiEntities ->
                    val categories = categoryApiEntities.mapNotNull { apiEntity -> CategoryViewEntity.ApiMapper.toViewEntity(apiEntity) }
                    _allCategories.value = categories
                    _performUiEvent.call(CategoriesUiEvent.DisplayLoadingState(LoadingState.Success))
                }
        }
    */}

}