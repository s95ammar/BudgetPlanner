package com.s95ammar.budgetplanner.ui.appscreens.categories.createedit

import androidx.fragment.app.viewModels
import com.s95ammar.budgetplanner.R
import com.s95ammar.budgetplanner.models.Resource
import com.s95ammar.budgetplanner.models.Result
import com.s95ammar.budgetplanner.models.data.Category
import com.s95ammar.budgetplanner.ui.appscreens.categories.createedit.data.CategoryInputBundle
import com.s95ammar.budgetplanner.ui.appscreens.categories.createedit.validation.CategoryCreateEditErrors
import com.s95ammar.budgetplanner.ui.appscreens.categories.createedit.validation.CategoryCreateEditViewKeys
import com.s95ammar.budgetplanner.ui.base.BaseFragment
import com.s95ammar.budgetplanner.ui.common.CreateEditMode
import com.s95ammar.budgetplanner.ui.common.validation.ValidationErrors
import com.s95ammar.budgetplanner.util.inputText
import com.s95ammar.budgetplanner.util.lifecycleutil.observeEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_category_create_edit.*

@AndroidEntryPoint
class CategoryCreateEditFragment : BaseFragment(R.layout.fragment_category_create_edit) {

    private val viewModel: CategoryCreateEditViewModel by viewModels()

    override fun setUpViews() {
        super.setUpViews()
        toolbar_category_create_edit.setNavigationOnClickListener { navController.navigateUp() }
        button_category_create_edit.setOnClickListener { onApply() }
    }

    override fun initObservers() {
        super.initObservers()
        viewModel.mode.observe(viewLifecycleOwner) { setViewsToMode(it) }
        viewModel.editedCategory.observe(viewLifecycleOwner) { handleEditedCategoryLoading(it) }
        viewModel.onCreateEditApply.observeEvent(viewLifecycleOwner) { handleCreateEditResult(it) }
        viewModel.onViewValidationError.observeEvent(viewLifecycleOwner) { handleValidationErrors(it) }
    }

    private fun setViewsToMode(mode: CreateEditMode) {
        when (mode) {
            CreateEditMode.CREATE -> {
                toolbar_category_create_edit.title = getString(R.string.create_category)
                button_category_create_edit.text = getString(R.string.create)
            }
            CreateEditMode.EDIT -> {
                toolbar_category_create_edit.title = getString(R.string.edit_category)
                button_category_create_edit.text = getString(R.string.save)
            }
        }
    }

    private fun handleEditedCategoryLoading(categoryResource: Resource<Category>?) {
        when (categoryResource) {
            is Resource.Loading -> loadingManager?.showLoading()
            is Resource.Error -> {
                loadingManager?.hideLoading()
                displayErrorDialog(categoryResource.throwable)
            }
            is Resource.Success -> {
                loadingManager?.hideLoading()
                setViewsToEditedCategory(categoryResource.data)
            }
        }

    }

    private fun setViewsToEditedCategory(category: Category) {
        input_layout_category_create_edit_title.editText?.setText(category.name)
    }

    private fun handleValidationErrors(validationErrors: ValidationErrors) {
        for (viewErrors in validationErrors.viewsErrors) {
            if (viewErrors.errorsIds.isNotEmpty())
                displayError(viewErrors.viewKey, viewErrors.errorsIds.first())
        }
    }

    private fun handleCreateEditResult(result: Result) {
        when (result) {
            is Result.Loading -> loadingManager?.showLoading()
            is Result.Error -> {
                loadingManager?.hideLoading()
                displayErrorDialog(result.throwable)
            }
            is Result.Success -> {
                loadingManager?.hideLoading()
                navController.navigateUp()
            }
        }
    }

    private fun displayError(viewKey: Int, errorId: Int) {
        when (viewKey) {
            CategoryCreateEditViewKeys.VIEW_TITLE -> input_layout_category_create_edit_title.error = getErrorStringById(errorId)
        }
    }

    private fun getErrorStringById(errorId: Int) = when (errorId) {
        CategoryCreateEditErrors.EMPTY_TITLE -> getString(R.string.error_empty_field)
        else -> null
    }

    private fun onApply() {
        clearViewsValidation()
        viewModel.onApply(
            CategoryInputBundle(title = input_layout_category_create_edit_title.inputText.trim())
        )
    }

    private fun clearViewsValidation() {
        CategoryCreateEditViewKeys.values().forEach { displayError(it, ValidationErrors.ERROR_NONE) }
    }

}