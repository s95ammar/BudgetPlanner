package com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgetransactioncreateedit.subscreens.categoryselection

import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import com.s95ammar.budgetplanner.R
import com.s95ammar.budgetplanner.databinding.FragmentBudgetTransactionCategorySelectionBinding
import com.s95ammar.budgetplanner.ui.appscreens.categories.adapter.CategoriesListAdapter
import com.s95ammar.budgetplanner.ui.appscreens.categories.common.data.Category
import com.s95ammar.budgetplanner.ui.base.BaseFragment
import com.s95ammar.budgetplanner.ui.common.Keys
import com.s95ammar.budgetplanner.ui.common.LoadingState
import com.s95ammar.budgetplanner.ui.common.viewbinding.ViewBinder
import com.s95ammar.budgetplanner.util.lifecycleutil.observeEvent
import dagger.hilt.android.AndroidEntryPoint
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgetransactioncreateedit.subscreens.categoryselection.data.BudgetTransactionCategorySelectionUiEvent as UiEvent

@AndroidEntryPoint
class BudgetTransactionCategorySelectionFragment : BaseFragment(R.layout.fragment_budget_transaction_category_selection),
    ViewBinder<FragmentBudgetTransactionCategorySelectionBinding> {

    companion object {
        fun newInstance() = BudgetTransactionCategorySelectionFragment()
    }

    override val binding: FragmentBudgetTransactionCategorySelectionBinding
        get() = getBinding()

    override fun initViewBinding(view: View): FragmentBudgetTransactionCategorySelectionBinding {
        return FragmentBudgetTransactionCategorySelectionBinding.bind(view)
    }

    private val viewModel: BudgetTransactionCategorySelectionViewModel by viewModels()

    private val adapter by lazy { CategoriesListAdapter(viewModel::onCategoryItemClick, onItemLongClick = null) }

    override fun setUpViews() {
        super.setUpViews()
        binding.recyclerView.adapter = adapter
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
    }

    override fun initObservers() {
        super.initObservers()
        viewModel.categories.observe(viewLifecycleOwner) { setAllCategories(it) }
        viewModel.performUiEvent.observeEvent(viewLifecycleOwner) { performUiEvent(it) }
    }

    private fun setAllCategories(categories: List<Category>) {
        adapter.submitList(categories)
    }

    private fun performUiEvent(uiEvent: UiEvent) {
        when (uiEvent) {
            is UiEvent.DisplayLoadingState -> handleLoadingState(uiEvent.loadingState)
            is UiEvent.SetResult -> setResult(uiEvent.category)
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

    private fun setResult(category: Category) {
        setFragmentResult(Keys.KEY_ON_CATEGORY_SELECTED, bundleOf(Keys.KEY_CATEGORY to category))
    }

}