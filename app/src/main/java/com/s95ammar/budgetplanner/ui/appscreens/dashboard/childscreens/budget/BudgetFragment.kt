package com.s95ammar.budgetplanner.ui.appscreens.dashboard.childscreens.budget

import android.view.View
import androidx.fragment.app.viewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import com.s95ammar.budgetplanner.R
import com.s95ammar.budgetplanner.databinding.FragmentDashboardBudgetBinding
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.DashboardSharedViewModel
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.childscreens.budget.adapter.PeriodicCategoriesListAdapter
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.common.data.PeriodicCategory
import com.s95ammar.budgetplanner.ui.base.BaseFragment
import com.s95ammar.budgetplanner.ui.common.viewbinding.ViewBinder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BudgetFragment : BaseFragment(R.layout.fragment_dashboard_budget), ViewBinder<FragmentDashboardBudgetBinding> {

    companion object {
        fun newInstance() = BudgetFragment()
    }

    private val viewModel: BudgetViewModel by viewModels()
    private val sharedViewModel: DashboardSharedViewModel by hiltNavGraphViewModels(R.id.nested_navigation_dashboard)

    private val adapter by lazy { PeriodicCategoriesListAdapter() }

    override val binding: FragmentDashboardBudgetBinding
        get() = getBinding()

    override fun initViewBinding(view: View): FragmentDashboardBudgetBinding {
        return FragmentDashboardBudgetBinding.bind(view)
    }

    override fun setUpViews() {
        super.setUpViews()
        binding.fab.setOnClickListener { sharedViewModel.onEditSelectedPeriod() }
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        binding.recyclerView.adapter = adapter
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
    }

    override fun initObservers() {
        super.initObservers()
        sharedViewModel.isPeriodAvailable.observe(viewLifecycleOwner) { setFabVisibility(it) }
        sharedViewModel.currentPeriodicRecords.observe(viewLifecycleOwner) { setPeriodicCategories(it) }
    }

    private fun setFabVisibility(isVisible: Boolean) {
        if (isVisible) binding.fab.show() else binding.fab.hide()
    }

    private fun setPeriodicCategories(periodicCategories: List<PeriodicCategory>) {
        adapter.submitList(periodicCategories)
    }

}