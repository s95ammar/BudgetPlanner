package com.s95ammar.budgetplanner.ui.appscreens.categories.createedit

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.s95ammar.budgetplanner.models.Resource
import com.s95ammar.budgetplanner.models.Result
import com.s95ammar.budgetplanner.models.ResultStateListener
import com.s95ammar.budgetplanner.models.repository.LocalRepository
import com.s95ammar.budgetplanner.ui.appscreens.categories.createedit.data.CategoryInputBundle
import com.s95ammar.budgetplanner.ui.appscreens.categories.createedit.validation.CategoryCreateEditErrors
import com.s95ammar.budgetplanner.ui.appscreens.categories.createedit.validation.CategoryCreateEditViewKeys
import com.s95ammar.budgetplanner.ui.common.CreateEditMode
import com.s95ammar.budgetplanner.ui.common.Keys
import com.s95ammar.budgetplanner.ui.common.validation.ValidationErrors
import com.s95ammar.budgetplanner.ui.common.validation.ValidationResult
import com.s95ammar.budgetplanner.ui.common.validation.Validator
import com.s95ammar.budgetplanner.ui.common.validation.ViewValidation
import com.s95ammar.budgetplanner.util.NO_ITEM
import com.s95ammar.budgetplanner.util.lifecycleutil.EventMutableLiveData
import com.s95ammar.budgetplanner.util.lifecycleutil.asLiveData
import kotlinx.coroutines.launch

class CategoryCreateEditViewModel @ViewModelInject constructor(
    private val localRepository: LocalRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

/*
    private val editedCategoryId = savedStateHandle.get<Int>(Keys.KEY_CATEGORY_ID) ?: Int.NO_ITEM
    private val _mode = MutableLiveData(CreateEditMode.getById(editedCategoryId))
    private val _onCreateEditApply = EventMutableLiveData<Result>()
    private val _onViewValidationError = EventMutableLiveData<ValidationErrors>()

    val mode = _mode.asLiveData()
    val onCreateEditApply = _onCreateEditApply.asEventLiveData()
    val onViewValidationError = _onViewValidationError.asEventLiveData()

    val editedCategory = liveData<Resource<Category>?> {
        if (editedCategoryId != Int.NO_ITEM) {
            emit(Resource.Loading())
            try {
                emitSource(localRepository.getCategoryByIdLiveData(editedCategoryId).map { Resource.Success(it) })
            } catch (e: Exception) {
                emit(Resource.Error(e))
            }
        }
    }

    fun onApply(categoryInputBundle: CategoryInputBundle) {
        val validator = createValidator(categoryInputBundle)

        when (val validationResult = validator.getValidationResult()) {
            is ValidationResult.Success -> onValidationSuccessful(validationResult.outputData)
            is ValidationResult.Error -> onValidationError(validationResult.throwable)
        }

    }

    private fun onValidationSuccessful(category: Category) {
        insertOrReplaceCategory(
            category = category,
            listener = object : ResultStateListener<Unit> {
                override fun onLoading() = _onCreateEditApply.call(Result.Loading)
                override fun onSuccess(data: Unit?) = _onCreateEditApply.call(Result.Success)
                override fun onError(throwable: Throwable) = _onCreateEditApply.call(Result.Error(throwable))
            }
        )

    }

    private fun onValidationError(validationErrors: ValidationErrors) {
        _onViewValidationError.call(validationErrors)
    }

    private fun createValidator(categoryInputBundle: CategoryInputBundle): Validator<CategoryInputBundle, Category> {

        return object : Validator<CategoryInputBundle, Category>(categoryInputBundle) {
            override fun provideOutputEntity(inputEntity: CategoryInputBundle) = Category(inputEntity.title)
                .apply { if (editedCategoryId != Int.NO_ITEM) id = editedCategoryId }

            override fun provideViewValidationList(): List<ViewValidation> {
                val caseEmptyTitle = ViewValidation.Case(CategoryCreateEditErrors.EMPTY_TITLE) { categoryInputBundle.title.isEmpty() }

                return listOf(
                    ViewValidation(CategoryCreateEditViewKeys.VIEW_TITLE, listOf(caseEmptyTitle))
                )
            }
        }
    }

    private fun insertOrReplaceCategory(category: Category, listener: ResultStateListener<Unit>) = viewModelScope.launch {
        try {
            listener.onLoading()
            localRepository.insertOrReplaceCategory(category)
            listener.onSuccess()
        } catch (e: Exception) {
            listener.onError(e)
        }
    }
*/

}