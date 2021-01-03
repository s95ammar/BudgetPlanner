package com.s95ammar.budgetplanner.ui.appscreens.categories.createedit

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.s95ammar.budgetplanner.models.api.requests.CategoryUpsertApiRequest
import com.s95ammar.budgetplanner.models.mappers.CategoryApiViewMapper
import com.s95ammar.budgetplanner.models.repository.LocalRepository
import com.s95ammar.budgetplanner.models.repository.RemoteRepository
import com.s95ammar.budgetplanner.models.view.CategoryViewEntity
import com.s95ammar.budgetplanner.ui.appscreens.auth.common.LoadingState
import com.s95ammar.budgetplanner.ui.appscreens.categories.createedit.data.CategoryInputBundle
import com.s95ammar.budgetplanner.ui.appscreens.categories.createedit.validation.CategoryCreateEditValidator
import com.s95ammar.budgetplanner.ui.common.CreateEditMode
import com.s95ammar.budgetplanner.ui.common.validation.ValidationErrors
import com.s95ammar.budgetplanner.util.NO_ITEM
import com.s95ammar.budgetplanner.util.lifecycleutil.EventMutableLiveData
import com.s95ammar.budgetplanner.util.lifecycleutil.EventMutableLiveDataVoid
import com.s95ammar.budgetplanner.util.lifecycleutil.LoaderMutableLiveData
import com.s95ammar.budgetplanner.util.lifecycleutil.asLiveData
import kotlinx.coroutines.launch

class CategoryCreateEditViewModel @ViewModelInject constructor(
    private val localRepository: LocalRepository,
    private val remoteRepository: RemoteRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
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
            remoteRepository.getCategory(editedCategoryId)
                .onSuccess { categoryApiEntity ->
                    categoryApiEntity.orEmpty()
                        .map { apiEntity -> CategoryApiViewMapper.toViewEntity(apiEntity) }
                        .singleOrNull()
                        ?.let { category ->
                            _editedCategory.value = category
                            _displayLoadingState.call(LoadingState.Success)
                        }
                }
                .onError { _displayLoadingState.call(LoadingState.Error(it)) }
        }

    }

    private fun onValidationSuccessful(category: CategoryUpsertApiRequest) = viewModelScope.launch {
        _displayLoadingState.call(LoadingState.Loading)
        val result = when (category) {
            is CategoryUpsertApiRequest.Insertion -> remoteRepository.insertCategory(category)
            is CategoryUpsertApiRequest.Update -> remoteRepository.updateCategory(category)
        }

        result
            .onSuccess {
                _onApplySuccess.call()
                _displayLoadingState.call(LoadingState.Success)
            }
            .onError { throwable ->
                _displayLoadingState.call(LoadingState.Error(throwable))
            }
    }

    private fun onValidationError(validationErrors: ValidationErrors) {
        _displayValidationResults.call(validationErrors)
    }


}