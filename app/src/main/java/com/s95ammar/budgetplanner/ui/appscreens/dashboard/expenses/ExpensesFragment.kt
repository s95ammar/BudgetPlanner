package com.s95ammar.budgetplanner.ui.appscreens.dashboard.expenses

import android.view.View
import androidx.fragment.app.viewModels
import com.s95ammar.budgetplanner.R
import com.s95ammar.budgetplanner.databinding.FragmentDashboardExpensesBinding
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.DashboardSharedViewModel
import com.s95ammar.budgetplanner.ui.base.BaseFragment
import com.s95ammar.budgetplanner.ui.common.viewbinding.ViewBinder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExpensesFragment : BaseFragment(R.layout.fragment_dashboard_expenses), ViewBinder<FragmentDashboardExpensesBinding> {

    companion object {
        fun newInstance() = ExpensesFragment()
    }

    private val viewModel: ExpensesViewModel by viewModels()
    private val sharedViewModel: DashboardSharedViewModel by viewModels(ownerProducer = { requireParentFragment() })

    override val binding: FragmentDashboardExpensesBinding
        get() = getBinding()

    override fun initViewBinding(view: View): FragmentDashboardExpensesBinding {
        return FragmentDashboardExpensesBinding.bind(view)
    }

    override fun setUpViews() {
        super.setUpViews()
    }

    override fun initObservers() {
        super.initObservers()
        sharedViewModel.selectedPeriodId.observe(viewLifecycleOwner) { viewModel.onPeriodChanged(it) }
    }

}