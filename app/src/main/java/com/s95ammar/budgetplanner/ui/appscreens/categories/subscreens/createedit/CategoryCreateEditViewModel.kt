package com.s95ammar.budgetplanner.ui.appscreens.categories.subscreens.createedit

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.s95ammar.budgetplanner.models.api.requests.CategoryUpsertApiRequest
import com.s95ammar.budgetplanner.models.repository.CategoriesRepository
import com.s95ammar.budgetplanner.ui.appscreens.auth.common.LoadingState
import com.s95ammar.budgetplanner.ui.appscreens.categories.common.data.CategoryViewEntity
import com.s95ammar.budgetplanner.ui.appscreens.categories.subscreens.createedit.data.CategoryInputBundle
import com.s95ammar.budgetplanner.ui.appscreens.categories.subscreens.createedit.validation.CategoryCreateEditValidator
import com.s95ammar.budgetplanner.ui.common.CreateEditMode
import com.s95ammar.budgetplanner.ui.common.validation.ValidationErrors
import com.s95ammar.budgetplanner.util.NO_ITEM
import com.s95ammar.budgetplanner.util.lifecycleutil.EventMutableLiveData
import com.s95ammar.budgetplanner.util.lifecycleutil.EventMutableLiveDataVoid
import com.s95ammar.budgetplanner.util.lifecycleutil.LoaderMutableLiveData
import com.s95ammar.budgetplanner.util.lifecycleutil.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryCreateEditViewModel @Inject constructor(
    private val repository: CategoriesRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val editedCategoryId = savedStateHandle.get<Int>(CategoryCreateEditFragmentArgs::categoryId.name) ?: Int.NO_ITEM

    private val _mode = MutableLiveData(CreateEditMode.getById(editedCategoryId))
    private val _editedCategory = LoaderMutableLiveData<CategoryViewEntity> { if (_mode.value == CreateEditMode.EDIT) loadEditedCategory() }
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
            _displayLoadingState.call(LoadingState.Loading)
            repository.getCategory(editedCategoryId)
                .catch { _displayLoadingState.call(LoadingState.Error(it)) }
                .collect { categoryApiEntity ->
                    CategoryViewEntity.ApiMapper.toViewEntity(categoryApiEntity)?.let { category ->
                        _editedCategory.value = category
                        _displayLoadingState.call(LoadingState.Success)
                    }
                }
        }

    }

    private fun onValidationSuccessful(request: CategoryUpsertApiRequest) = viewModelScope.launch {
        _displayLoadingState.call(LoadingState.Loading)
        val flowRequest = when (request) {
            is CategoryUpsertApiRequest.Insertion -> repository.insertCategory(request)
            is CategoryUpsertApiRequest.Update -> repository.updateCategory(request)
        }

        flowRequest
            .catch { throwable ->
                _displayLoadingState.call(LoadingState.Error(throwable))
            }
            .collect {
                _onApplySuccess.call()
                _displayLoadingState.call(LoadingState.Success)
            }
    }

    private fun onValidationError(validationErrors: ValidationErrors) {
        _displayValidationResults.call(validationErrors)
    }


}