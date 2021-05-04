package com.s95ammar.budgetplanner.ui.appscreens.dashboard.pager.budget

import android.view.View
import androidx.fragment.app.viewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import com.s95ammar.budgetplanner.R
import com.s95ammar.budgetplanner.databinding.FragmentDashboardBudgetBinding
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.DashboardSharedViewModel
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.common.data.PeriodicCategory
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.pager.budget.adapter.PeriodicCategoriesListAdapter
import com.s95ammar.budgetplanner.ui.common.viewbinding.BaseViewBinderFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BudgetFragment : BaseViewBinderFragment<FragmentDashboardBudgetBinding>(R.layout.fragment_dashboard_budget) {

    companion object {
        fun newInstance() = BudgetFragment()
    }

    private val viewModel: BudgetViewModel by viewModels()
    private val sharedViewModel: DashboardSharedViewModel by hiltNavGraphViewModels(R.id.nested_navigation_dashboard)

    private val adapter by lazy { PeriodicCategoriesListAdapter() }

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
        sharedViewModel.currentPeriodicCategories.observe(viewLifecycleOwner) { setPeriodicCategories(it) }
    }

    private fun setPeriodicCategories(periodicCategories: List<PeriodicCategory>) {
        adapter.submitList(periodicCategories)
    }

}