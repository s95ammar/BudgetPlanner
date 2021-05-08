package com.s95ammar.budgetplanner.ui.appscreens.dashboard.pager.budgettransactions

import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import com.s95ammar.budgetplanner.R
import com.s95ammar.budgetplanner.databinding.FragmentDashboardTransactionsBinding
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.DashboardSharedViewModel
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.common.data.BudgetTransaction
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.pager.budget.data.BudgetUiEvent
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.pager.budgettransactions.adapter.BudgetTransactionsListAdapter
import com.s95ammar.budgetplanner.ui.common.LoadingState
import com.s95ammar.budgetplanner.ui.common.viewbinding.BaseViewBinderFragment
import com.s95ammar.budgetplanner.util.lifecycleutil.observeEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BudgetTransactionsFragment : BaseViewBinderFragment<FragmentDashboardTransactionsBinding>(R.layout.fragment_dashboard_transactions) {

    companion object {
        fun newInstance() = BudgetTransactionsFragment()
    }

    private val viewModel: BudgetTransactionsViewModel by viewModels()
    private val sharedViewModel: DashboardSharedViewModel by hiltNavGraphViewModels(R.id.nested_navigation_dashboard)

    private val adapter by lazy { BudgetTransactionsListAdapter() }

    override fun initViewBinding(view: View): FragmentDashboardTransactionsBinding {
        return FragmentDashboardTransactionsBinding.bind(view)
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
        viewModel.budgetTransactions.observe(viewLifecycleOwner) { setBudgetTransactions(it) }
        sharedViewModel.selectedPeriodId.observe(viewLifecycleOwner) { viewModel.onPeriodChanged(it) }

        viewModel.performUiEvent.observeEvent(viewLifecycleOwner) { performUiEvent(it) }
    }

    private fun setBudgetTransactions(budgetTransactions: List<BudgetTransaction>) {
        adapter.submitList(budgetTransactions)
    }

    private fun performUiEvent(uiEvent: BudgetUiEvent) {
        when (uiEvent) {
            is BudgetUiEvent.DisplayLoadingState -> handleLoadingState(uiEvent.loadingState)
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

    override fun showLoading() {
        binding.progressBar.isVisible = true
    }

    override fun hideLoading() {
        binding.progressBar.isGone = true
    }
}