package com.s95ammar.budgetplanner.ui.appscreens.dashboard.pager.budget

import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import com.s95ammar.budgetplanner.R
import com.s95ammar.budgetplanner.databinding.FragmentDashboardBudgetBinding
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.DashboardSharedViewModel
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.common.data.PeriodicCategory
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.pager.budget.adapter.PeriodicCategoriesProgressAdapter
import com.s95ammar.budgetplanner.ui.common.LoadingState
import com.s95ammar.budgetplanner.ui.common.viewbinding.BaseViewBinderFragment
import com.s95ammar.budgetplanner.util.lifecycleutil.observeEvent
import dagger.hilt.android.AndroidEntryPoint
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.pager.budget.data.BudgetUiEvent as UiEvent

@AndroidEntryPoint
class BudgetFragment : BaseViewBinderFragment<FragmentDashboardBudgetBinding>(R.layout.fragment_dashboard_budget) {

    companion object {
        fun newInstance() = BudgetFragment()
    }

    private val viewModel: BudgetViewModel by viewModels()
    private val sharedViewModel: DashboardSharedViewModel by hiltNavGraphViewModels(R.id.nested_navigation_dashboard)

    private val adapter by lazy { PeriodicCategoriesProgressAdapter() }

    override fun initViewBinding(view: View): FragmentDashboardBudgetBinding {
        return FragmentDashboardBudgetBinding.bind(view)
    }

    override fun setUpViews() {
        super.setUpViews()
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        binding.recyclerView.adapter = adapter
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
    }

    override fun initObservers() {
        super.initObservers()
        viewModel.periodicCategories.observe(viewLifecycleOwner) { setPeriodicCategories(it) }
        sharedViewModel.selectedPeriodId.observe(viewLifecycleOwner) { viewModel.onPeriodChanged(it) }

        viewModel.performUiEvent.observeEvent(viewLifecycleOwner) { performUiEvent(it) }
    }

    private fun setPeriodicCategories(periodicCategories: List<PeriodicCategory>) {
        adapter.submitList(periodicCategories)
        binding.recyclerView.isGone = periodicCategories.isEmpty()
        binding.instructionsLayout.root.isVisible = periodicCategories.isEmpty()
        binding.instructionsLayout.messageTextView.text = getString(R.string.instruction_budget_screen_no_data)

    }

    private fun performUiEvent(uiEvent: UiEvent) {
        when (uiEvent) {
            is UiEvent.DisplayLoadingState -> handleLoadingState(uiEvent.loadingState)
            is UiEvent.NavigateToEditBudgetTransaction -> sharedViewModel
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

    override fun showLoading() {
        binding.progressBar.isVisible = true
    }

    override fun hideLoading() {
        binding.progressBar.isGone = true
    }
}