package com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budget

import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import com.s95ammar.budgetplanner.R
import com.s95ammar.budgetplanner.databinding.FragmentDashboardBudgetBinding
import com.s95ammar.budgetplanner.models.view.PeriodRecordViewEntity
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budget.adapter.PeriodRecordsListAdapter
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budget.data.BudgetUiEvent
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.DashboardSharedViewModel
import com.s95ammar.budgetplanner.ui.base.BaseFragment
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

    private val adapter by lazy { PeriodRecordsListAdapter() }

    override val binding: FragmentDashboardBudgetBinding
        get() = getBinding()

    override fun initViewBinding(view: View): FragmentDashboardBudgetBinding {
        return FragmentDashboardBudgetBinding.bind(view)
    }

    override fun setUpViews() {
        super.setUpViews()
        binding.fab.setOnClickListener { viewModel.onEditPeriod() }
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
        viewModel.resultPeriodRecords.observe(viewLifecycleOwner) { setPeriodRecords(it) }
        viewModel.performUiEvent.observeEvent(viewLifecycleOwner) { performUiEvent(it) }
        sharedViewModel.selectedPeriodId.observe(viewLifecycleOwner) { viewModel.onSelectedPeriodChanged(it) }
        sharedViewModel.onPeriodRecordsLoaded.observeEvent(viewLifecycleOwner) { (periodId, periodRecords) ->
            viewModel.onPeriodRecordsLoaded(periodId, periodRecords)
        }
//        sharedViewModel.onPeriodRecordsChanged.observeEvent(viewLifecycleOwner) { viewModel.refresh() }
    }

    private fun showFabIfPeriodIsAvailable(periodId: Int) {
        if (periodId == Int.NO_ITEM) binding.fab.hide() else binding.fab.show()
    }

    private fun setPeriodRecords(periodRecords: List<PeriodRecordViewEntity>) {
        adapter.submitList(periodRecords)
    }

    private fun performUiEvent(uiEvent: BudgetUiEvent) {
        when (uiEvent) {
            is BudgetUiEvent.OnEditPeriod -> onEditPeriod(uiEvent.periodId)
        }
    }

    private fun onEditPeriod(periodId: Int) {
        sharedViewModel.onEditPeriod(periodId)
    }
}