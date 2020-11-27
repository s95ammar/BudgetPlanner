package com.s95ammar.budgetplanner.ui.appscreens.dashboard

import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.s95ammar.budgetplanner.R
import com.s95ammar.budgetplanner.ui.common.viewpagerhelpers.FragmentProvider
import com.s95ammar.budgetplanner.ui.common.viewpagerhelpers.ViewPagerFragmentAdapter
import com.s95ammar.budgetplanner.ui.base.BaseFragment
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.budget.BudgetFragment
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.savings.SavingsFragment
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.expenses.ExpensesFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_dashboard.*

@AndroidEntryPoint
class DashboardFragment : BaseFragment(R.layout.fragment_dashboard) {

    private val viewModel: DashboardViewModel by viewModels()

    override fun setUpViews() {
        super.setUpViews()
        pager.adapter = ViewPagerFragmentAdapter(
            this,
            listOf(
                FragmentProvider { BudgetFragment.newInstance() },
                FragmentProvider { ExpensesFragment.newInstance() },
                FragmentProvider { SavingsFragment.newInstance() }
            )
        )
        val titles = listOf(
            getString(R.string.budget),
            getString(R.string.expenses),
            getString(R.string.savings)
        )
        TabLayoutMediator(tab_layout, pager) { tab, position ->
            tab.text = titles[position]
        }.attach()
    }
}