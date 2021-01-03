package com.s95ammar.budgetplanner.ui.appscreens.categories.createedit

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import com.s95ammar.budgetplanner.R
import com.s95ammar.budgetplanner.databinding.FragmentCategoryCreateEditBinding
import com.s95ammar.budgetplanner.models.api.responses.errors.ConflictError
import com.s95ammar.budgetplanner.models.view.CategoryViewEntity
import com.s95ammar.budgetplanner.ui.appscreens.auth.common.LoadingState
import com.s95ammar.budgetplanner.ui.appscreens.categories.createedit.data.CategoryInputBundle
import com.s95ammar.budgetplanner.ui.appscreens.categories.createedit.validation.CategoryCreateEditValidator
import com.s95ammar.budgetplanner.ui.base.BaseFragment
import com.s95ammar.budgetplanner.ui.common.CreateEditMode
import com.s95ammar.budgetplanner.ui.common.Keys
import com.s95ammar.budgetplanner.ui.common.validation.ValidationErrors
import com.s95ammar.budgetplanner.ui.common.viewbinding.ViewBinder
import com.s95ammar.budgetplanner.util.inputText
import com.s95ammar.budgetplanner.util.lifecycleutil.observeEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryCreateEditFragment : BaseFragment(R.layout.fragment_category_create_edit), ViewBinder<FragmentCategoryCreateEditBinding> {

    private val viewModel: CategoryCreateEditViewModel by viewModels()

    override val binding: FragmentCategoryCreateEditBinding
        get() = getBinding()

    override fun initViewBinding(view: View): FragmentCategoryCreateEditBinding {
        return FragmentCategoryCreateEditBinding.bind(view)
    }

    override fun setUpViews() {
        super.setUpViews()
        binding.toolbar.setNavigationOnClickListener { navController.navigateUp() }
        binding.buttonApply.setOnClickListener { onApply() }
    }

    override fun initObservers() {
        super.initObservers()
        viewModel.mode.observe(viewLifecycleOwner) { setViewsToMode(it) }
        viewModel.editedCategory.observe(viewLifecycleOwner) { setViewsToEditedCategory(it) }
        viewModel.displayLoadingState.observeEvent(viewLifecycleOwner) { handleLoadingState(it) }
        viewModel.displayValidationResults.observeEvent(viewLifecycleOwner) { handleValidationErrors(it) }
        viewModel.onApplySuccess.observeEvent(viewLifecycleOwner) { onApplySuccess() }
    }

    private fun setViewsToMode(mode: CreateEditMode) {
        when (mode) {
            CreateEditMode.CREATE -> {
                binding.toolbar.title = getString(R.string.create_category)
                binding.buttonApply.text = getString(R.string.create)
            }
            CreateEditMode.EDIT -> {
                binding.toolbar.title = getString(R.string.edit_category)
                binding.buttonApply.text = getString(R.string.save)
            }
        }
    }

    private fun handleLoadingState(loadingState: LoadingState) {
        when (loadingState) {
            is LoadingState.Cold,
            is LoadingState.Success -> hideLoading()
            is LoadingState.Loading -> showLoading()
            is LoadingState.Error -> {
                hideLoading()
                handleError(loadingState.throwable)
            }
        }
    }

    private fun handleError(throwable: Throwable) {
        when (throwable) {
            is ConflictError -> displayError(CategoryCreateEditValidator.ViewKeys.VIEW_TITLE, CategoryCreateEditValidator.Errors.NAME_TAKEN)
            else -> showErrorToast(throwable)
        }
    }

    private fun setViewsToEditedCategory(category: CategoryViewEntity) {
        binding.inputLayoutTitle.editText?.apply {
            setText(category.name)
            setSelection(category.name.length)
        }
    }

    private fun handleValidationErrors(validationErrors: ValidationErrors) {
        for (viewErrors in validationErrors.viewsErrors) {
            if (viewErrors.errorsIds.isNotEmpty())
                displayError(viewErrors.viewKey, viewErrors.highestPriorityOrNone)
        }
    }

    private fun displayError(viewKey: Int, errorId: Int) {
        when (viewKey) {
            CategoryCreateEditValidator.ViewKeys.VIEW_TITLE -> binding.inputLayoutTitle.error = getErrorStringById(errorId)
            CategoryCreateEditValidator.Errors.NAME_TAKEN -> binding.inputLayoutTitle.error = getErrorStringById(errorId)
        }
    }

    private fun getErrorStringById(errorId: Int) = when (errorId) {
        CategoryCreateEditValidator.Errors.EMPTY_TITLE -> getString(R.string.error_empty_field)
        CategoryCreateEditValidator.Errors.NAME_TAKEN -> getString(R.string.error_category_name_taken)
        else -> null
    }

    private fun onApplySuccess() {
        setFragmentResult(Keys.KEY_ON_CATEGORY_CREATE_EDIT, Bundle.EMPTY)
        navController.navigateUp()
    }

    private fun onApply() {
        viewModel.onApply(
            CategoryInputBundle(title = binding.inputLayoutTitle.inputText.orEmpty().trim())
        )
    }

}