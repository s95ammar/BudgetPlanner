package com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.periodcreateedit

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import com.s95ammar.budgetplanner.R
import com.s95ammar.budgetplanner.databinding.FragmentPeriodCreateEditBinding
import com.s95ammar.budgetplanner.ui.appscreens.auth.common.LoadingState
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.common.data.PeriodicCategoryViewEntity
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.periodcreateedit.data.PeriodCreateEditUiEvent
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.periodcreateedit.data.PeriodInputBundle
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.periodcreateedit.validation.PeriodCreateEditValidator
import com.s95ammar.budgetplanner.ui.base.BaseFragment
import com.s95ammar.budgetplanner.ui.common.CreateEditMode
import com.s95ammar.budgetplanner.ui.common.Keys
import com.s95ammar.budgetplanner.ui.common.validation.ValidationErrors
import com.s95ammar.budgetplanner.ui.common.viewbinding.ViewBinder
import com.s95ammar.budgetplanner.util.inputText
import com.s95ammar.budgetplanner.util.lifecycleutil.observeEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PeriodCreateEditFragment : BaseFragment(R.layout.fragment_period_create_edit), ViewBinder<FragmentPeriodCreateEditBinding> {

    private val viewModel: PeriodCreateEditViewModel by viewModels()
    private val sharedViewModel: PeriodCreateEditSharedViewModel by hiltNavGraphViewModels(R.id.nested_period_create_edit)

    override val binding: FragmentPeriodCreateEditBinding
        get() = getBinding()

    override fun initViewBinding(view: View): FragmentPeriodCreateEditBinding {
        return FragmentPeriodCreateEditBinding.bind(view)
    }

    override fun setUpViews() {
        super.setUpViews()
        binding.toolbar.setNavigationOnClickListener { viewModel.onBack() }
        binding.textViewPeriodCategoriesValue.setOnClickListener { viewModel.onChooseCategories() }
        binding.buttonApply.setOnClickListener { sharedViewModel.onApply(getPeriodInputBundle()) }
    }

    override fun initObservers() {
        super.initObservers()
        sharedViewModel.mode.observe(viewLifecycleOwner) { setViewsToMode(it) }
        sharedViewModel.name.observe(viewLifecycleOwner) { setPeriodName(it) }
        sharedViewModel.max.observe(viewLifecycleOwner) { setPeriodMax(it) }
        sharedViewModel.selectedPeriodicCategories.observe(viewLifecycleOwner) { setCategoriesNamesStringValue(it) }

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
        binding.inputLayoutName.editText?.apply {
            setText(name)
            setSelection(name.length)
        }
    }

    private fun setPeriodMax(max: Int?) {
        binding.inputLayoutMax.inputText = max?.toString()
    }

    private fun setCategoriesNamesStringValue(items: List<PeriodicCategoryViewEntity>) {
        val isEmpty = items.isEmpty()

        binding.textViewPeriodCategoriesValue.text = if (isEmpty) {
            getString(R.string.choose_categories)
        } else {
            buildString {
                for ((i, periodicCategory) in items.withIndex()) {
                    if (i != 0) append(", ")
                    append(periodicCategory.categoryName)
                }
            }
        }
        binding.textViewPeriodCategoriesValue.setTextColor(
            ContextCompat.getColor(requireContext(), if (isEmpty) R.color.colorGray else R.color.colorBlack)
        )
    }

    private fun performUiEvent(uiEvent: PeriodCreateEditUiEvent) {
        when (uiEvent) {
            is PeriodCreateEditUiEvent.DisplayLoadingState -> handleLoadingState(uiEvent.loadingState)
            is PeriodCreateEditUiEvent.SetResult -> setResult()
            is PeriodCreateEditUiEvent.DisplayValidationResults -> handleValidationErrors(uiEvent.validationErrors)
            is PeriodCreateEditUiEvent.Exit -> navController.navigateUp()
            is PeriodCreateEditUiEvent.ChooseCategories -> navigateToCategoriesSelection()
        }
    }

    private fun handleLoadingState(loadingState: LoadingState) {
        when (loadingState) {
            is LoadingState.Cold,
            is LoadingState.Success -> hideLoading()
            is LoadingState.Loading -> showLoading()
            is LoadingState.Error -> {
                hideLoading()
                showErrorToast(loadingState.throwable)
            }
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
            PeriodCreateEditValidator.ViewKeys.VIEW_NAME -> binding.inputLayoutName.error = getErrorStringById(errorId)
        }
    }

    private fun getErrorStringById(errorId: Int) = when (errorId) {
        PeriodCreateEditValidator.Errors.EMPTY_NAME -> getString(R.string.error_empty_field)
        else -> null
    }

    private fun setResult() {
        setFragmentResult(Keys.KEY_PERIODIC_CATEGORIES_SCREEN_ON_PERIODS_LIST_CHANGED, Bundle.EMPTY)
        setFragmentResult(Keys.KEY_ON_PERIOD_CREATE_EDIT, Bundle.EMPTY)
    }

    private fun navigateToCategoriesSelection() {
        navController.navigate(R.id.action_periodCreateEditFragment_to_periodCategoriesSelectionFragment)
    }

    private fun getPeriodInputBundle() = PeriodInputBundle(
        name = binding.inputLayoutName.inputText.orEmpty().trim(),
        max = binding.inputLayoutMax.inputText?.trim()
    )

}