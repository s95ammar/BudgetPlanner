package com.s95ammar.budgetplanner.ui.appscreens.categories.subscreens.createedit

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.CategoryEntity
import com.s95ammar.budgetplanner.models.repository.CategoriesRepository
import com.s95ammar.budgetplanner.ui.appscreens.categories.common.data.Category
import com.s95ammar.budgetplanner.ui.appscreens.categories.subscreens.createedit.data.CategoryInputBundle
import com.s95ammar.budgetplanner.ui.appscreens.categories.subscreens.createedit.validation.CategoryCreateEditValidator
import com.s95ammar.budgetplanner.ui.common.CreateEditMode
import com.s95ammar.budgetplanner.ui.common.LoadingState
import com.s95ammar.budgetplanner.ui.common.validation.ValidationErrors
import com.s95ammar.budgetplanner.util.NO_ITEM
import com.s95ammar.budgetplanner.util.lifecycleutil.EventMutableLiveData
import com.s95ammar.budgetplanner.util.lifecycleutil.EventMutableLiveDataVoid
import com.s95ammar.budgetplanner.util.lifecycleutil.LoaderMutableLiveData
import com.s95ammar.budgetplanner.util.lifecycleutil.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryCreateEditViewModel @Inject constructor(
    private val repository: CategoriesRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val editedCategoryId = savedStateHandle.get<Int>(CategoryCreateEditFragmentArgs::categoryId.name) ?: Int.NO_ITEM

    private val _mode = MutableLiveData(CreateEditMode.getById(editedCategoryId))
    private val _editedCategory = LoaderMutableLiveData<Category> { if (_mode.value == CreateEditMode.EDIT) loadEditedCategory() }
    // TODO: move events to a sealed class
    private val _displayLoadingState = EventMutableLiveData<LoadingState>(LoadingState.Cold)
    private val _onApplySuccess = EventMutableLiveDataVoid()
    private val _displayValidationResults = EventMutableLiveData<ValidationErrors>()

    val mode = _mode.asLiveData()
    val editedCategory = _editedCategory.asLiveData()
    val displayLoadingState = _displayLoadingState.asEventLiveData()
    val onApplySuccess = _onApplySuccess.asEventLiveData()
    val displayValidationResults = _displayValidationResults.asEventLiveData()

    fun onApply(categoryInputBundle: CategoryInputBundle) {
        val validator = CategoryCreateEditValidator(editedCategoryId, categoryInputBundle)
        _displayValidationResults.call(validator.getValidationErrors(allBlank = true))

        validator.getValidationResult()
            .onSuccess { onValidationSuccessful(it) }
            .onError { onValidationError(it) }

    }

    private fun loadEditedCategory() {
        viewModelScope.launch {
            repository.getCategoryFlow(editedCategoryId)
                .onStart {
                    _displayLoadingState.call(LoadingState.Loading)
                }
                .catch { throwable ->
                    _displayLoadingState.call(LoadingState.Error(throwable))
                }
                .collect { categoryEntity ->
                    Category.Mapper.fromEntity(categoryEntity)?.let { category ->
                        _editedCategory.value = category
                        _displayLoadingState.call(LoadingState.Success)
                    }
                }
        }

    }

    private fun onValidationSuccessful(category: CategoryEntity) = viewModelScope.launch {
        val flowRequest = when (_mode.value) {
            CreateEditMode.CREATE -> repository.insertCategoryFlow(category)
            CreateEditMode.EDIT -> repository.updateCategoryFlow(category)
            else -> return@launch
        }

        flowRequest
            .onStart {
                _displayLoadingState.call(LoadingState.Loading)
            }
            .catch { throwable ->
                _displayLoadingState.call(LoadingState.Error(throwable))
            }
            .collect {
                _displayLoadingState.call(LoadingState.Success)
                _onApplySuccess.call()
            }
    }

    private fun onValidationError(validationErrors: ValidationErrors) {
        _displayValidationResults.call(validationErrors)
    }


}