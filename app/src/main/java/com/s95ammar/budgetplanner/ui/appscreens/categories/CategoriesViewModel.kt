package com.s95ammar.budgetplanner.ui.appscreens.categories

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.s95ammar.budgetplanner.models.Resource
import com.s95ammar.budgetplanner.models.Result
import com.s95ammar.budgetplanner.models.ResultStateListener
import com.s95ammar.budgetplanner.models.repository.LocalRepository
import com.s95ammar.budgetplanner.util.lifecycleutil.EventMutableLiveData
import kotlinx.coroutines.launch

class CategoriesViewModel @ViewModelInject constructor(
    private val localRepository: LocalRepository
) : ViewModel() {

/*
    private val _navigateToEditCategory = EventMutableLiveData<Int>()
    private val _showBottomSheet = EventMutableLiveData<Category>()
    private val _displayDeleteResultState = EventMutableLiveData<Result>()

    val navigateToEditCategory = _navigateToEditCategory.asEventLiveData()
    val showBottomSheet = _showBottomSheet.asEventLiveData()
    val displayDeleteResultState = _displayDeleteResultState.asEventLiveData()

    val allCategories = liveData {
        emit(Resource.Loading())
        try {
            emitSource(localRepository.getAllCategoriesLiveData().map { Resource.Success(it) })
        } catch (e: Exception) {
            emit(Resource.Error<List<Category>>(e))
        }
    }

    fun onCategoryItemClick(position: Int) {
        allCategories.list?.getOrNull(position)?.let { category ->
            _navigateToEditCategory.call(category.id)
        }
    }

    fun onCategoryItemLongClick(position: Int) {
        allCategories.list?.getOrNull(position)?.let { category ->
            _showBottomSheet.call(category)
        }
    }

    fun onDeleteCategory(id: Int) = viewModelScope.launch {
        deleteCategory(categoryId = id, listener = object : ResultStateListener<Unit> {
            override fun onLoading() = _displayDeleteResultState.call(Result.Loading)
            override fun onSuccess(data: Unit?) = _displayDeleteResultState.call(Result.Success)
            override fun onError(throwable: Throwable) = _displayDeleteResultState.call(Result.Error(throwable))
        })
    }

    private fun deleteCategory(categoryId: Int, listener: ResultStateListener<Unit>)  = viewModelScope.launch {
        try {
            listener.onLoading()
            val category = localRepository.getCategoryById(categoryId)
            localRepository.deleteCategory(category)
            listener.onSuccess()
        } catch (e: Exception) {
            listener.onError(e)
        }
    }

    private val LiveData<Resource<List<Category>>>.list: List<Category>?
        get() = (allCategories.value as? Resource.Success)?.data
*/

}