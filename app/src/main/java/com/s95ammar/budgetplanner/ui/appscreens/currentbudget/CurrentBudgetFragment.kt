package com.s95ammar.budgetplanner.ui.appscreens.currentbudget

import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.s95ammar.budgetplanner.R
import com.s95ammar.budgetplanner.ui.common.viewpagerhelpers.FragmentProvider
import com.s95ammar.budgetplanner.ui.common.viewpagerhelpers.ViewPagerFragmentAdapter
import com.s95ammar.budgetplanner.ui.base.BaseFragment
import com.s95ammar.budgetplanner.ui.appscreens.currentbudget.dashboard.DashboardFragment
import com.s95ammar.budgetplanner.ui.appscreens.currentbudget.savings.SavingsFragment
import com.s95ammar.budgetplanner.ui.appscreens.currentbudget.expenses.ExpensesFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_current_budget.*

@AndroidEntryPoint
class CurrentBudgetFragment : BaseFragment(R.layout.fragment_current_budget) {

    private val viewModel: CurrentBudgetViewModel by viewModels()

    override fun setUpViews() {
        super.setUpViews()
        pager.adapter = ViewPagerFragmentAdapter(
            this,
            listOf(
                FragmentProvider { DashboardFragment.newInstance() },
                FragmentProvider { ExpensesFragment.newInstance() },
                FragmentProvider { SavingsFragment.newInstance() }
            )
        )
        val titles = listOf(
            getString(R.string.dashboard),
            getString(R.string.expenses),
            getString(R.string.savings)
        )
        TabLayoutMediator(tab_layout, pager) { tab, position ->
            tab.text = titles[position]
        }.attach()
    }
}