package com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgetransactioncreateedit.subscreens.categoryselection

import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import com.s95ammar.budgetplanner.R
import com.s95ammar.budgetplanner.databinding.FragmentBudgetTransactionCategorySelectionBinding
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgetransactioncreateedit.data.PeriodicCategoryIdAndName
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgetransactioncreateedit.subscreens.categoryselection.adapter.PeriodicCategoriesSingleSelectionAdapter
import com.s95ammar.budgetplanner.ui.common.Keys
import com.s95ammar.budgetplanner.ui.common.LoadingState
import com.s95ammar.budgetplanner.ui.common.viewbinding.BaseViewBinderFragment
import com.s95ammar.budgetplanner.util.lifecycleutil.observeEvent
import dagger.hilt.android.AndroidEntryPoint
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgetransactioncreateedit.subscreens.categoryselection.data.BudgetTransactionCategorySelectionUiEvent as UiEvent

@AndroidEntryPoint
class BudgetTransactionCategorySelectionFragment :
    BaseViewBinderFragment<FragmentBudgetTransactionCategorySelectionBinding>(R.layout.fragment_budget_transaction_category_selection) {

    companion object {
        fun newInstance() = BudgetTransactionCategorySelectionFragment()
    }

    override fun initViewBinding(view: View): FragmentBudgetTransactionCategorySelectionBinding {
        return FragmentBudgetTransactionCategorySelectionBinding.bind(view)
    }

    private val viewModel: BudgetTransactionCategorySelectionViewModel by viewModels()

    private val adapter by lazy { PeriodicCategoriesSingleSelectionAdapter(viewModel::onPeriodicCategoryItemClick) }

    override fun setUpViews() {
        super.setUpViews()
        binding.toolbar.setNavigationOnClickListener { navController.navigateUp() }
        binding.recyclerView.adapter = adapter
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
    }

    override fun initObservers() {
        super.initObservers()
        viewModel.periodicCategories.observe(viewLifecycleOwner) { setPeriodicCategories(it) }
        viewModel.performUiEvent.observeEvent(viewLifecycleOwner) { performUiEvent(it) }
    }

    private fun setPeriodicCategories(periodicCategories: List<PeriodicCategoryIdAndName>) {
        adapter.submitList(periodicCategories)
        binding.recyclerView.isGone = periodicCategories.isEmpty()
        binding.instructionsLayout.root.isVisible = periodicCategories.isEmpty()
        binding.instructionsLayout.messageTextView.text = getString(R.string.instruction_add_periodic_category)
    }

    private fun performUiEvent(uiEvent: UiEvent) {
        when (uiEvent) {
            is UiEvent.DisplayLoadingState -> handleLoadingState(uiEvent.loadingState)
            is UiEvent.SetResult -> setResult(uiEvent.periodicCategory)
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

    private fun setResult(periodicCategory: PeriodicCategoryIdAndName) {
        setFragmentResult(Keys.KEY_ON_PERIODIC_CATEGORY_SELECTED, bundleOf(Keys.KEY_PERIODIC_CATEGORY to periodicCategory))
    }

}