package com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.periodcreateedit

import android.database.sqlite.SQLiteConstraintException
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import com.s95ammar.budgetplanner.R
import com.s95ammar.budgetplanner.databinding.FragmentPeriodCreateEditBinding
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.common.data.CategoryOfPeriod
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.periodcreateedit.data.PeriodInputBundle
import com.s95ammar.budgetplanner.ui.common.CreateEditMode
import com.s95ammar.budgetplanner.ui.common.LoadingState
import com.s95ammar.budgetplanner.ui.common.validation.ValidationErrors
import com.s95ammar.budgetplanner.ui.common.viewbinding.BaseViewBinderFragment
import com.s95ammar.budgetplanner.util.getStringOrNull
import com.s95ammar.budgetplanner.util.lifecycleutil.observeEvent
import com.s95ammar.budgetplanner.util.text
import com.s95ammar.budgetplanner.util.updateTextIfNotEquals
import dagger.hilt.android.AndroidEntryPoint
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.periodcreateedit.data.PeriodCreateEditUiEvent as UiEvent
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.periodcreateedit.validation.PeriodCreateEditValidator as Validator

@AndroidEntryPoint
class PeriodCreateEditFragment : BaseViewBinderFragment<FragmentPeriodCreateEditBinding>(R.layout.fragment_period_create_edit) {

    private val viewModel: PeriodCreateEditViewModel by viewModels()
    private val sharedViewModel: PeriodCreateEditSharedViewModel by hiltNavGraphViewModels(R.id.nested_period_create_edit)

    override fun initViewBinding(view: View): FragmentPeriodCreateEditBinding {
        return FragmentPeriodCreateEditBinding.bind(view)
    }

    override fun setUpViews() {
        super.setUpViews()
        binding.toolbar.setNavigationOnClickListener { viewModel.onBack() }
        binding.textViewPeriodCategoriesValue.setOnClickListener { viewModel.onChooseCategories() }
        binding.buttonApply.setOnClickListener { sharedViewModel.onApply(createPeriodInputBundle()) }
        binding.inputLayoutName.editText?.doAfterTextChanged { sharedViewModel.setName(it?.toString().orEmpty()) }
    }

    override fun initObservers() {
        super.initObservers()
        sharedViewModel.mode.observe(viewLifecycleOwner) { setViewsToMode(it) }
        sharedViewModel.name.observe(viewLifecycleOwner) { setPeriodName(it) }
        sharedViewModel.selectedCategoriesOfPeriod.observe(viewLifecycleOwner) { setCategoriesNamesStringValue(it) }

        viewModel.performUiEvent.observeEvent(viewLifecycleOwner) { performUiEvent(it) }
        sharedViewModel.performUiEvent.observeEvent(viewLifecycleOwner) { performUiEvent(it) }
    }

    private fun setViewsToMode(mode: CreateEditMode) {
        when (mode) {
            CreateEditMode.CREATE -> {
                binding.toolbar.title = getString(R.string.create_period)
                binding.buttonApply.text = getString(R.string.create)
            }
            CreateEditMode.EDIT -> {
                binding.toolbar.title = getString(R.string.edit_period)
                binding.buttonApply.text = getString(R.string.save)
            }
        }
    }

    private fun setPeriodName(name: String) {
        binding.inputLayoutName.updateTextIfNotEquals(name)
    }

    private fun setCategoriesNamesStringValue(items: List<CategoryOfPeriod>) {
        val isEmpty = items.isEmpty()

        binding.textViewPeriodCategoriesValue.text = if (isEmpty) {
            getString(R.string.choose_categories)
        } else {
            buildString {
                for ((i, categoryOfPeriod) in items.withIndex()) {
                    if (i != 0) append(", ")
                    append(categoryOfPeriod.categoryName)
                }
            }
        }
        binding.textViewPeriodCategoriesValue.setTextColor(
            ContextCompat.getColor(requireContext(), if (isEmpty) R.color.colorDarkGray else R.color.colorBlack)
        )
    }

    private fun performUiEvent(uiEvent: UiEvent) {
        when (uiEvent) {
            is UiEvent.DisplayLoadingState -> handleLoadingState(uiEvent.loadingState)
            is UiEvent.DisplayValidationResults -> handleValidationErrors(uiEvent.validationErrors)
            is UiEvent.Exit -> navController.navigateUp()
            is UiEvent.ChooseCategories -> navigateToCategoriesSelection()
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
            else -> showErrorSnackbar(throwable)
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
            Validator.ViewKeys.VIEW_NAME -> binding.inputLayoutName.error = getStringOrNull(errorId)
        }
    }

    private fun navigateToCategoriesSelection() {
        navController.navigate(R.id.action_periodCreateEditFragment_to_periodCategoriesSelectionFragment)
    }

    private fun createPeriodInputBundle() = PeriodInputBundle(
        name = binding.inputLayoutName.text.orEmpty().trim()
    )

}