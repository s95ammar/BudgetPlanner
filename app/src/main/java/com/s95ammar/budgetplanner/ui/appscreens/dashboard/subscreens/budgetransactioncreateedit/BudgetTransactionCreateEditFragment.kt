package com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgetransactioncreateedit

import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import com.s95ammar.budgetplanner.R
import com.s95ammar.budgetplanner.databinding.FragmentBudgetTransactionCreateEditBinding
import com.s95ammar.budgetplanner.models.IntBudgetTransactionType
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgetransactioncreateedit.data.BudgetTransactionInputBundle
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgetransactioncreateedit.data.CategoryOfPeriodSimple
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgetransactioncreateedit.subscreens.locationselection.data.LocationWithAddress
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgetransactioncreateedit.validation.BudgetTransactionCreateEditValidator
import com.s95ammar.budgetplanner.ui.common.CreateEditMode
import com.s95ammar.budgetplanner.ui.common.Keys
import com.s95ammar.budgetplanner.ui.common.LoadingState
import com.s95ammar.budgetplanner.ui.common.validation.ValidationErrors
import com.s95ammar.budgetplanner.ui.common.viewbinding.BaseViewBinderFragment
import com.s95ammar.budgetplanner.util.doOnTabSelected
import com.s95ammar.budgetplanner.util.getAmountStringFormatted
import com.s95ammar.budgetplanner.util.lifecycleutil.observeEvent
import com.s95ammar.budgetplanner.util.setDrawableTint
import com.s95ammar.budgetplanner.util.text
import com.s95ammar.budgetplanner.util.toDoubleOrNull
import com.s95ammar.budgetplanner.util.updateTextIfNotEquals
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale
import javax.inject.Inject
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgetransactioncreateedit.data.BudgetTransactionCreateEditUiEvent as UiEvent

@AndroidEntryPoint
class BudgetTransactionCreateEditFragment :
    BaseViewBinderFragment<FragmentBudgetTransactionCreateEditBinding>(R.layout.fragment_budget_transaction_create_edit) {

    private val viewModel: BudgetTransactionCreateEditViewModel by viewModels()

    @Inject
    lateinit var locale: Locale

    override fun initViewBinding(view: View): FragmentBudgetTransactionCreateEditBinding {
        return FragmentBudgetTransactionCreateEditBinding.bind(view)
    }

    override fun setUpViews() {
        super.setUpViews()
        binding.toolbar.setNavigationOnClickListener { viewModel.onBack() }
        setUpTabLayout()
        binding.currencyTextView.setOnClickListener { viewModel.onCalculateByAnotherCurrency() }
        binding.textViewPeriodCategoryValue.setOnClickListener { viewModel.onChooseCategoryOfPeriod() }
        binding.textViewLocationValue.setOnClickListener { viewModel.onChooseLocation() }
        binding.buttonApply.setOnClickListener { viewModel.onApply(getBudgetTransactionInputBundle()) }
        binding.inputLayoutName.editText?.doAfterTextChanged { viewModel.setName(it?.toString().orEmpty()) }
        binding.inputLayoutAmount.editText?.doAfterTextChanged { viewModel.setAmount(it?.toString().orEmpty()) }
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
                viewModel.setType(IntBudgetTransactionType.getByPosition(tab.position))
            }
        }
    }

    override fun initObservers() {
        super.initObservers()
        viewModel.mode.observe(viewLifecycleOwner) { setViewsToMode(it) }
        viewModel.type.observe(viewLifecycleOwner) { setType(it) }
        viewModel.name.observe(viewLifecycleOwner) { setName(it) }
        viewModel.amountInput.observe(viewLifecycleOwner) { setAmount(it) }
        viewModel.isCurrencyAvailable.observe(viewLifecycleOwner) { setIsCurrencyAvailable(it) }
        viewModel.currencyCode.observe(viewLifecycleOwner) { setCurrencyCode(it) }
        viewModel.categoryOfPeriod.observe(viewLifecycleOwner) { setSelectedCategoryOfPeriod(it) }
        viewModel.locationOptional.observe(viewLifecycleOwner) { setSelectedLocation(it?.value) }
        viewModel.performUiEvent.observeEvent(viewLifecycleOwner) { performUiEvent(it) }
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

    private fun setType(@IntBudgetTransactionType type: Int) {
        val position = IntBudgetTransactionType.getPosition(type)
        val tab = binding.tabLayout.getTabAt(position)
        binding.tabLayout.selectTab(tab)
    }

    private fun setName(name: String) {
        binding.inputLayoutName.updateTextIfNotEquals(name)
    }

    private fun setAmount(amount: Double) {
        if (amount != binding.inputLayoutAmount.text?.toDoubleOrNull(locale)) {
            binding.inputLayoutAmount.updateTextIfNotEquals(
                getAmountStringFormatted(amount, isForEditText = true)
            )
        }
    }

    private fun setIsCurrencyAvailable(isCurrencyAvailable: Boolean) {
        binding.currencyTextView.isVisible = isCurrencyAvailable
    }

    private fun setCurrencyCode(currencyCode: String) {
        binding.currencyTextView.text = currencyCode
    }

    private fun setSelectedCategoryOfPeriod(categoryOfPeriod: CategoryOfPeriodSimple) {
        binding.textViewPeriodCategoryValue.text = categoryOfPeriod.categoryName
        binding.textViewPeriodCategoryValue.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorBlack))
    }

    private fun setSelectedLocation(location: LocationWithAddress?) {
        binding.textViewLocationValue.text = location?.address ?: getString(R.string.choose_location_optional)
        val textColorRes = if (location == null) R.color.colorDarkGray else R.color.colorBlack
        binding.textViewLocationValue.setTextColor(ContextCompat.getColor(requireContext(), textColorRes))
    }

    private fun performUiEvent(uiEvent: UiEvent) {
        when (uiEvent) {
            is UiEvent.NavigateToCurrencyConversion -> listenAndNavigateToCurrencyConversion(uiEvent.currentCurrencyCode)
            is UiEvent.ChooseCategoryOfPeriod -> listenAndNavigateToCategoryOfPeriodSelection(uiEvent.periodId)
            is UiEvent.ChooseLocation -> listenAndNavigateToLocationSelection(uiEvent.currentLocation)
            is UiEvent.DisplayValidationResults -> handleValidationErrors(uiEvent.validationErrors)
            is UiEvent.DisplayLoadingState -> handleLoadingState(uiEvent.loadingState)
            is UiEvent.Exit -> navController.navigateUp()
        }
    }

    private fun handleLoadingState(loadingState: LoadingState) {
        when (loadingState) {
            is LoadingState.Cold,
            is LoadingState.Success -> hideLoading()
            is LoadingState.Loading -> showLoading()
            is LoadingState.Error -> {
                hideLoading()
                showErrorSnackbar(loadingState.throwable)
            }
        }
    }

    private fun listenAndNavigateToCurrencyConversion(currencyCode: String) {
        setFragmentResultListener(Keys.KEY_AMOUNT_REQUEST) { _, bundle ->
            bundle.getDouble(Keys.KEY_AMOUNT).let { amount ->
                viewModel.onAmountCalculated(amount)
            }
        }
        navController.navigate(
            BudgetTransactionCreateEditFragmentDirections
                .actionBudgetTransactionCreateEditFragmentToCurrencyConversionFragment(currencyCode)
        )
    }

    private fun listenAndNavigateToCategoryOfPeriodSelection(periodId: Int) {
        setFragmentResultListener(Keys.KEY_CATEGORY_OF_PERIOD_REQUEST) { _, bundle ->
            bundle.getParcelable<CategoryOfPeriodSimple>(Keys.KEY_CATEGORY_OF_PERIOD)?.let { categoryOfPeriod ->
                viewModel.setCategoryOfPeriod(categoryOfPeriod)
            }
        }
        navController.navigate(
            BudgetTransactionCreateEditFragmentDirections
                .actionBudgetTransactionCreateEditFragmentToBudgetTransactionCategorySelectionFragment(periodId)
        )
    }

    private fun listenAndNavigateToLocationSelection(location: LocationWithAddress?) {
        setFragmentResultListener(Keys.KEY_LOCATION_REQUEST) { _, bundle ->
            viewModel.setLocation(bundle.getParcelable(Keys.KEY_LOCATION))
        }
        navController.navigate(
            BudgetTransactionCreateEditFragmentDirections
                .actionBudgetTransactionCreateEditFragmentToLocationSelectionFragment(location)
        )
    }

    private fun handleValidationErrors(validationErrors: ValidationErrors) {
        for (viewErrors in validationErrors.viewsErrors) {
            if (viewErrors.errorsIds.isNotEmpty())
                displayError(viewErrors.viewKey, viewErrors.highestPriorityOrNone)
        }
    }

    private fun displayError(viewKey: Int, errorId: Int) {
        when (viewKey) {
            BudgetTransactionCreateEditValidator.ViewKeys.VIEW_NAME -> binding.inputLayoutName.error = getErrorStringById(errorId)
            BudgetTransactionCreateEditValidator.ViewKeys.VIEW_AMOUNT -> binding.inputLayoutAmount.error = getErrorStringById(errorId)
            BudgetTransactionCreateEditValidator.ViewKeys.VIEW_CATEGORY -> {
                val colorText = ContextCompat.getColor(
                    requireContext(),
                    if (errorId == ValidationErrors.ERROR_NONE) R.color.colorBlack else R.color.colorError
                )
                val colorDrawable = ContextCompat.getColor(
                    requireContext(),
                    if (errorId == ValidationErrors.ERROR_NONE) R.color.colorDarkGray else R.color.colorErrorLight
                )
                binding.textViewPeriodCategoryValue.setTextColor(colorText)
                binding.textViewPeriodCategoryValue.setDrawableTint(colorDrawable)
            }
        }
    }

    private fun getErrorStringById(errorId: Int) = when (errorId) {
        BudgetTransactionCreateEditValidator.Errors.EMPTY_NAME,
        BudgetTransactionCreateEditValidator.Errors.EMPTY_AMOUNT -> getString(R.string.error_empty_field)
        else -> null
    }

    private fun getBudgetTransactionInputBundle() = BudgetTransactionInputBundle(
        name = binding.inputLayoutName.text?.trim().orEmpty(),
        amount = binding.inputLayoutAmount.text?.trim().orEmpty()
    )

}
