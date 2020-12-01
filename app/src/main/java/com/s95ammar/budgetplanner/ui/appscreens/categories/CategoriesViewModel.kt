package com.s95ammar.budgetplanner.ui.appscreens.categories

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.s95ammar.budgetplanner.models.mappers.CategoryApiViewMapper
import com.s95ammar.budgetplanner.models.repository.LocalRepository
import com.s95ammar.budgetplanner.models.repository.RemoteRepository
import com.s95ammar.budgetplanner.models.view.CategoryViewEntity
import com.s95ammar.budgetplanner.ui.appscreens.auth.common.LoadingState
import com.s95ammar.budgetplanner.util.lifecycleutil.*
import kotlinx.coroutines.launch

class CategoriesViewModel @ViewModelInject constructor(
    private val localRepository: LocalRepository,
    private val remoteRepository: RemoteRepository
) : ViewModel() {

    private val _allCategories = LoaderMutableLiveData<List<CategoryViewEntity>> { loadAllCategories() }
    private val _displayLoadingState = EventMutableLiveData<LoadingState>(LoadingState.Cold)
    private val _navigateToEditCategory = EventMutableLiveData<Int>()
    private val _showBottomSheet = EventMutableLiveData<CategoryViewEntity>()

    val allCategories = _allCategories.asLiveData()
    val displayLoadingState = _displayLoadingState.asEventLiveData()
    val navigateToEditCategory = _navigateToEditCategory.asEventLiveData()
    val showBottomSheet = _showBottomSheet.asEventLiveData()

    fun refresh() {
        loadAllCategories()
    }

    fun onCategoryItemClick(position: Int) {
        _allCategories.value?.getOrNull(position)?.let { category ->
            _navigateToEditCategory.call(category.id)
        }
    }

    fun onCategoryItemLongClick(position: Int) {
        _allCategories.value?.getOrNull(position)?.let { category ->
            _showBottomSheet.call(category)
        }
    }

    fun onDeleteCategory(id: Int) = viewModelScope.launch {
        _displayLoadingState.call(LoadingState.Loading)
        remoteRepository.deleteCategory(id)
            .onSuccess { refresh() }
            .onError { _displayLoadingState.call(LoadingState.Error(it)) }
    }

    private fun loadAllCategories() {
        viewModelScope.launch {
            _displayLoadingState.call(LoadingState.Loading)
            remoteRepository.getAllUserCategories()
                .onSuccess { categoryApiEntities ->
                    val categories = categoryApiEntities.orEmpty().mapNotNull { apiEntity -> CategoryApiViewMapper.toViewEntity(apiEntity) }
                    _allCategories.value = categories
                    _displayLoadingState.call(LoadingState.Success)
                }
                .onError { _displayLoadingState.call(LoadingState.Error(it)) }
        }
    }

}