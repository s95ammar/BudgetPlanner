package com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.periodcreateedit.subscreens.categoriesselection.subscreens

import android.view.View
import androidx.core.os.bundleOf
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import com.s95ammar.budgetplanner.R
import com.s95ammar.budgetplanner.databinding.FragmentPeriodCategoryEstimateCreateEditBinding
import com.s95ammar.budgetplanner.models.IntBudgetTransactionType
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.periodcreateedit.subscreens.categoriesselection.subscreens.data.PeriodCategoryEstimateCreateEditUiEvent
import com.s95ammar.budgetplanner.ui.common.Keys
import com.s95ammar.budgetplanner.ui.common.viewbinding.BaseViewBinderFragment
import com.s95ammar.budgetplanner.util.Optional
import com.s95ammar.budgetplanner.util.doOnTabSelected
import com.s95ammar.budgetplanner.util.getAmountStringFormatted
import com.s95ammar.budgetplanner.util.lifecycleutil.observeEvent
import com.s95ammar.budgetplanner.util.text
import com.s95ammar.budgetplanner.util.updateTextIfNotEquals
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PeriodCategoryEstimateCreateEditFragment
    : BaseViewBinderFragment<FragmentPeriodCategoryEstimateCreateEditBinding>(
    R.layout.fragment_period_category_estimate_create_edit
) {

    private val viewModel: PeriodCategoryEstimateCreateEditViewModel by viewModels()

    override fun initViewBinding(view: View): FragmentPeriodCategoryEstimateCreateEditBinding {
        return FragmentPeriodCategoryEstimateCreateEditBinding.bind(view)
    }

    override fun setUpViews() {
        binding.toolbar.setNavigationOnClickListener { viewModel.onBack() }
        setUpTabLayout()
        binding.buttonApply.setOnClickListener { viewModel.onApply() }
        binding.estimateInputLayout.editText?.doAfterTextChanged { viewModel.setEstimate(it?.toString().orEmpty()) }
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
        viewModel.categoryName.observe(viewLifecycleOwner) { setCategoryName(it) }
        viewModel.estimateInput.observe(viewLifecycleOwner) { setEstimate(it) }
        viewModel.currencyCode.observe(viewLifecycleOwner) { setCurrencyCode(it) }
        viewModel.type.observe(viewLifecycleOwner) { setType(it) }
        viewModel.performUiEvent.observeEvent(viewLifecycleOwner) { performUiEvent(it) }
    }

    private fun setCategoryName(categoryName: String) {
        binding.categoryNameTextView.text = categoryName
    }

    private fun setEstimate(estimateOptional: Optional<Double>) {
        estimateOptional.value?.takeIf { it != 0.0 }?.let { estimate ->
            if (estimate != binding.estimateInputLayout.text?.toDoubleOrNull()) {
                binding.estimateInputLayout.updateTextIfNotEquals(
                    getAmountStringFormatted(estimate, isForEditText = true)
                )
            }
        }
    }

    private fun setCurrencyCode(currencyCode: String) {
        binding.currencyTextView.text = currencyCode
    }

    private fun setType(@IntBudgetTransactionType type: Int) {
        val position = IntBudgetTransactionType.getPosition(type)
        val tab = binding.tabLayout.getTabAt(position)
        binding.tabLayout.selectTab(tab)
    }

    private fun performUiEvent(uiEvent: PeriodCategoryEstimateCreateEditUiEvent) {
        when (uiEvent) {
            is PeriodCategoryEstimateCreateEditUiEvent.SetResult -> setResult(uiEvent.estimate)
            is PeriodCategoryEstimateCreateEditUiEvent.Exit -> navController.navigateUp()
        }
    }

    private fun setResult(estimate: Double?) {
        setFragmentResult(Keys.KEY_ESTIMATE_REQUEST, bundleOf(Keys.KEY_ESTIMATE to estimate))
    }
}