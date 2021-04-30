package com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgetransactioncreateedit

import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import com.s95ammar.budgetplanner.R
import com.s95ammar.budgetplanner.databinding.FragmentBudgetTransactionCreateEditBinding
import com.s95ammar.budgetplanner.models.IntBudgetTransactionType
import com.s95ammar.budgetplanner.ui.appscreens.categories.common.data.CategoryViewEntity
import com.s95ammar.budgetplanner.ui.base.BaseFragment
import com.s95ammar.budgetplanner.ui.common.CreateEditMode
import com.s95ammar.budgetplanner.ui.common.Keys
import com.s95ammar.budgetplanner.ui.common.viewbinding.ViewBinder
import com.s95ammar.budgetplanner.util.doOnTabSelected
import com.s95ammar.budgetplanner.util.lifecycleutil.observeEvent
import dagger.hilt.android.AndroidEntryPoint
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgetransactioncreateedit.data.BudgetTransactionCreateEditUiEvent as UiEvent

@AndroidEntryPoint
class BudgetTransactionCreateEditFragment : BaseFragment(R.layout.fragment_budget_transaction_create_edit),
    ViewBinder<FragmentBudgetTransactionCreateEditBinding> {

    private val viewModel: BudgetTransactionCreateEditViewModel by viewModels()

    override val binding: FragmentBudgetTransactionCreateEditBinding
        get() = getBinding()

    override fun initViewBinding(view: View): FragmentBudgetTransactionCreateEditBinding {
        return FragmentBudgetTransactionCreateEditBinding.bind(view)
    }

    override fun setUpViews() {
        super.setUpViews()
        binding.toolbar.setNavigationOnClickListener { viewModel.onBack() }
        setUpTabLayout()
        binding.textViewPeriodCategoryValue.setOnClickListener { viewModel.onChooseCategory() }
//        binding.buttonApply.setOnClickListener { viewModel.onApply(getPeriodInputBundle()) }
    }

    private fun setUpTabLayout() {
        binding.tabLayout.apply {
            for (@IntBudgetTransactionType type in IntBudgetTransactionType.values()) {
                val tabTitle = when (type) {
                    IntBudgetTransactionType.EXPENSE -> getString(R.string.expense)
                    IntBudgetTransactionType.INCOME -> getString(R.string.income)
                    else -> null
                } ?: continue
                addTab(newTab().setText(tabTitle))
            }
            doOnTabSelected { tab ->
                viewModel.setSelectedBudgetTransactionType(tab.position)
            }
        }
    }

    override fun initObservers() {
        super.initObservers()
        viewModel.mode.observe(viewLifecycleOwner) { setViewsToMode(it) }
/*
        viewModel.name.observe(viewLifecycleOwner) { setPeriodName(it) }
        viewModel.max.observe(viewLifecycleOwner) { setPeriodMax(it) }
        viewModel.periodicCategories.observe(viewLifecycleOwner) { setPeriodicCategories(it) }
*/
        viewModel.selectedCategory.observe(viewLifecycleOwner) { setSelectedCategory(it) }
        viewModel.performUiEvent.observeEvent(viewLifecycleOwner) { performUiEvent(it) }
    }

    private fun setSelectedCategory(category: CategoryViewEntity) {
        binding.textViewPeriodCategoryValue.text = category.name
        binding.textViewPeriodCategoryValue.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorBlack))
    }

    private fun performUiEvent(uiEvent: UiEvent) {
        when (uiEvent) {
            is UiEvent.ChooseCategory -> onNavigateToCategorySelection()
            is UiEvent.DisplayValidationResults -> TODO()
            is UiEvent.Exit -> navController.navigateUp()
        }
    }

    private fun onNavigateToCategorySelection() {
        setFragmentResultListener(Keys.KEY_ON_CATEGORY_SELECTED) { _, bundle ->
            bundle.getParcelable<CategoryViewEntity>(Keys.KEY_CATEGORY)?.let { category ->
                viewModel.setCategory(category)
            }
        }
        navController.navigate(
            BudgetTransactionCreateEditFragmentDirections
                .actionBudgetTransactionCreateEditFragmentToBudgetTransactionCategorySelectionFragment()
        )
    }

    private fun setViewsToMode(mode: CreateEditMode) {
        when (mode) {
            CreateEditMode.CREATE -> {
                binding.toolbar.title = getString(R.string.create_budget_transaction)
                binding.buttonApply.text = getString(R.string.create)
            }
            CreateEditMode.EDIT -> {
                binding.toolbar.title = getString(R.string.edit_budget_transaction)
                binding.buttonApply.text = getString(R.string.save)
            }
        }
    }


/*
    private fun performUiEvent(uiEvent: PeriodCreateEditUiEvent) {
        when (uiEvent) {
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

    private fun getPeriodInputBundle() = PeriodInputBundle(
        name = binding.inputLayoutName.inputText.orEmpty().trim(),
        max = binding.inputLayoutMax.inputText?.trim()
    )
*/

}