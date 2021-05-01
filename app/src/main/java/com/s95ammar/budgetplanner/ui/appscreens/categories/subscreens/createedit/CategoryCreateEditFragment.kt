package com.s95ammar.budgetplanner.ui.appscreens.categories.subscreens.createedit

import android.database.sqlite.SQLiteConstraintException
import android.os.Bundle
import android.view.View
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import com.s95ammar.budgetplanner.R
import com.s95ammar.budgetplanner.databinding.FragmentCategoryCreateEditBinding
import com.s95ammar.budgetplanner.ui.appscreens.categories.subscreens.createedit.data.CategoryInputBundle
import com.s95ammar.budgetplanner.ui.common.CreateEditMode
import com.s95ammar.budgetplanner.ui.common.Keys
import com.s95ammar.budgetplanner.ui.common.LoadingState
import com.s95ammar.budgetplanner.ui.common.validation.ValidationErrors
import com.s95ammar.budgetplanner.ui.common.viewbinding.BaseViewBinderFragment
import com.s95ammar.budgetplanner.util.lifecycleutil.observeEvent
import com.s95ammar.budgetplanner.util.text
import dagger.hilt.android.AndroidEntryPoint
import com.s95ammar.budgetplanner.ui.appscreens.categories.subscreens.createedit.validation.CategoryCreateEditValidator as Validator

@AndroidEntryPoint
class CategoryCreateEditFragment : BaseViewBinderFragment<FragmentCategoryCreateEditBinding>(R.layout.fragment_category_create_edit) {

    private val viewModel: CategoryCreateEditViewModel by viewModels()

    override fun initViewBinding(view: View): FragmentCategoryCreateEditBinding {
        return FragmentCategoryCreateEditBinding.bind(view)
    }

    override fun setUpViews() {
        super.setUpViews()
        binding.toolbar.setNavigationOnClickListener { navController.navigateUp() }
        binding.buttonApply.setOnClickListener { viewModel.onApply(getCategoryInputBundle()) }
    }

    override fun initObservers() {
        super.initObservers()
        viewModel.mode.observe(viewLifecycleOwner) { setViewsToMode(it) }
        viewModel.name.observe(viewLifecycleOwner) { setCategoryName(it) }
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
            is SQLiteConstraintException -> displayError(Validator.ViewKeys.VIEW_NAME, Validator.Errors.NAME_TAKEN)
            else -> showErrorToast(throwable)
        }
    }

    private fun setCategoryName(name: String) {
        binding.inputLayoutName.editText?.apply {
            setText(name)
            setSelection(name.length)
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
            Validator.ViewKeys.VIEW_NAME -> binding.inputLayoutName.error = getErrorStringById(errorId)
            Validator.Errors.NAME_TAKEN -> binding.inputLayoutName.error = getErrorStringById(errorId)
        }
    }

    private fun getErrorStringById(errorId: Int) = when (errorId) {
        Validator.Errors.EMPTY_TITLE -> getString(R.string.error_empty_field)
        Validator.Errors.NAME_TAKEN -> getString(R.string.error_category_name_taken)
        else -> null
    }

    private fun onApplySuccess() {
        setFragmentResult(Keys.KEY_ON_CATEGORY_CREATE_EDIT, Bundle.EMPTY)
        navController.navigateUp()
    }

    private fun getCategoryInputBundle() = CategoryInputBundle(title = binding.inputLayoutName.text.orEmpty().trim())

}