package com.s95ammar.budgetplanner.ui.appscreens.categories.subscreens.createedit

import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.CategoryEntity
import com.s95ammar.budgetplanner.models.repository.CategoriesRepository
import com.s95ammar.budgetplanner.ui.appscreens.categories.common.data.Category
import com.s95ammar.budgetplanner.ui.appscreens.categories.subscreens.createedit.data.CategoryCreateEditUiEvent
import com.s95ammar.budgetplanner.ui.appscreens.categories.subscreens.createedit.data.CategoryInputBundle
import com.s95ammar.budgetplanner.ui.appscreens.categories.subscreens.createedit.validation.CategoryCreateEditValidator
import com.s95ammar.budgetplanner.ui.appscreens.categories.subscreens.createedit.validation.CategoryCreateEditValidator.Errors
import com.s95ammar.budgetplanner.ui.appscreens.categories.subscreens.createedit.validation.CategoryCreateEditValidator.ViewKeys
import com.s95ammar.budgetplanner.ui.common.CreateEditMode
import com.s95ammar.budgetplanner.ui.common.LoadingState
import com.s95ammar.budgetplanner.ui.common.validation.ValidationErrors
import com.s95ammar.budgetplanner.util.INVALID
import com.s95ammar.budgetplanner.util.addSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.s95ammar.budgetplanner.ui.appscreens.categories.subscreens.createedit.CategoryCreateEditFragmentArgs as FragmentArgs

@HiltViewModel
class CategoryCreateEditViewModel @Inject constructor(
    private val repository: CategoriesRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val editedCategoryId = savedStateHandle.get<Int>(FragmentArgs::categoryId.name) ?: Int.INVALID

    private val _mode = MutableStateFlow(CreateEditMode.getById(editedCategoryId))
    private val _editedCategory = MutableStateFlow<Category?>(null)
    private val _name = MutableStateFlow("")
        .addSource(_editedCategory, viewModelScope) { value = it?.name.orEmpty() }
    private val _validationErrors = MutableStateFlow<ValidationErrors?>(null)
    private val _uiEvent = MutableSharedFlow<CategoryCreateEditUiEvent>()

    val mode = _mode.asStateFlow()
    val name = _name.asStateFlow()
    val validationErrors = _validationErrors.asStateFlow()
    val uiEvent = _uiEvent.asSharedFlow()

    init {
        if (_mode.value == CreateEditMode.EDIT) loadEditedCategory()
    }

    fun onNameChanged(name: String) {
        _name.value = name
    }

    fun onApply() {
        viewModelScope.launch {
            val validator = CategoryCreateEditValidator(editedCategoryId, CategoryInputBundle(_name.value.trim()))
            _validationErrors.value = validator.getBlankValidationErrors()

            validator.getValidationResult().handle(
                onSuccess = { insertOrUpdateCategory(it) },
                onError = { _validationErrors.value = it }
            )
        }
    }

    fun onBack() {
        viewModelScope.launch {
            _uiEvent.emit(CategoryCreateEditUiEvent.Exit)
        }
    }

    private fun loadEditedCategory() {
        viewModelScope.launch {
            repository.getCategoryFlow(editedCategoryId)
                .onStart {
                    _uiEvent.emit(CategoryCreateEditUiEvent.DisplayLoadingState(LoadingState.Loading))
                }
                .catch { throwable ->
                    _uiEvent.emit(CategoryCreateEditUiEvent.DisplayLoadingState(LoadingState.Error(throwable)))
                }
                .first()
                .let { categoryEntity ->
                    Category.Mapper.fromEntity(categoryEntity)?.let { category ->
                        _editedCategory.value = category
                        _uiEvent.emit(CategoryCreateEditUiEvent.DisplayLoadingState(LoadingState.Success))
                    }
                }
        }

    }

    private fun insertOrUpdateCategory(category: CategoryEntity) = viewModelScope.launch {

        val flowRequest = when (_mode.value) {
            CreateEditMode.CREATE -> repository.insertCategoryFlow(category)
            CreateEditMode.EDIT -> repository.updateCategoryFlow(category)
        }

        flowRequest
            .onStart {
                _uiEvent.emit(CategoryCreateEditUiEvent.DisplayLoadingState(LoadingState.Loading))
            }
            .catch { throwable ->
                val isNameConstraintError = throwable is SQLiteConstraintException
                if (isNameConstraintError) {
                    val currentValidationErrors = _validationErrors.value
                    val nameConstraintError = ValidationErrors.ViewErrors(ViewKeys.VIEW_NAME, listOf(Errors.NAME_TAKEN))
                    _validationErrors.emit(
                        ValidationErrors(listOf(nameConstraintError)).merge(currentValidationErrors)
                    )
                }
                _uiEvent.emit(
                    CategoryCreateEditUiEvent.DisplayLoadingState(LoadingState.Error(throwable))
                )

            }
            .collect {
                _uiEvent.emit(CategoryCreateEditUiEvent.DisplayLoadingState(LoadingState.Success))
                _uiEvent.emit(CategoryCreateEditUiEvent.Exit)
            }
    }

}