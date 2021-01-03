package com.s95ammar.budgetplanner.ui.appscreens.dashboard.budget

import android.view.View
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import com.s95ammar.budgetplanner.R
import com.s95ammar.budgetplanner.databinding.FragmentDashboardBudgetBinding
import com.s95ammar.budgetplanner.models.view.PeriodRecordViewEntity
import com.s95ammar.budgetplanner.ui.appscreens.auth.common.LoadingState
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.DashboardSharedViewModel
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.budget.adapter.PeriodRecordsListAdapter
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.budget.data.PeriodRecordsNavigationBundle
import com.s95ammar.budgetplanner.ui.base.BaseFragment
import com.s95ammar.budgetplanner.ui.common.Keys
import com.s95ammar.budgetplanner.ui.common.viewbinding.ViewBinder
import com.s95ammar.budgetplanner.util.NO_ITEM
import com.s95ammar.budgetplanner.util.lifecycleutil.hiltNavGraphViewModels
import com.s95ammar.budgetplanner.util.lifecycleutil.observeEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BudgetFragment : BaseFragment(R.layout.fragment_dashboard_budget), ViewBinder<FragmentDashboardBudgetBinding> {

    companion object {
        fun newInstance() = BudgetFragment()
    }

    private val viewModel: BudgetViewModel by viewModels()
    private val sharedViewModel: DashboardSharedViewModel by hiltNavGraphViewModels(R.id.nested_navigation_dashboard)

    private val adapter by lazy { PeriodRecordsListAdapter(/*TODO*/) }

    override val binding: FragmentDashboardBudgetBinding
        get() = getBinding()

    override fun initViewBinding(view: View): FragmentDashboardBudgetBinding {
        return FragmentDashboardBudgetBinding.bind(view)
    }

    override fun setUpViews() {
        super.setUpViews()
        binding.fab.setOnClickListener { viewModel.onNavigateToPeriodRecords() }
        binding.swipeToRefreshLayout.setOnRefreshListener { viewModel.refresh() }
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        binding.recyclerView.adapter = adapter
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
    }

    override fun initObservers() {
        super.initObservers()
        viewModel.currentPeriodId.observe(viewLifecycleOwner) { showFabIfPeriodIsAvailable(it) }
        viewModel.periodRecords.observe(viewLifecycleOwner) { setPeriodRecords(it) }
        viewModel.displayLoadingState.observeEvent(viewLifecycleOwner) { handleLoadingState(it) }
        viewModel.navigateToPeriodRecords.observeEvent(viewLifecycleOwner) { navigateToPeriodRecords(it) }
        sharedViewModel.selectedPeriodId.observe(viewLifecycleOwner) { viewModel.onPeriodChanged(it) }
        setFragmentResultListener(Keys.KEY_ON_PERIOD_RECORD_ADDED) { _, _ -> viewModel.refresh() }
    }

    private fun showFabIfPeriodIsAvailable(periodId: Int) {
        if (periodId == Int.NO_ITEM) binding.fab.hide() else binding.fab.show()
    }

    private fun setPeriodRecords(periodRecords: List<PeriodRecordViewEntity>) {
        adapter.submitList(periodRecords)
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

    override fun showLoading() {
        binding.swipeToRefreshLayout.isRefreshing = true
    }

    override fun hideLoading() {
        binding.swipeToRefreshLayout.isRefreshing = false
    }

    private fun handleError(throwable: Throwable) {
        when (throwable) {
            else -> throwable.message?.let { showToast(it) }
        }
    }

    private fun navigateToPeriodRecords(navigationBundle: PeriodRecordsNavigationBundle) {
        sharedViewModel.navigateToPeriodRecords(navigationBundle)
    }
}