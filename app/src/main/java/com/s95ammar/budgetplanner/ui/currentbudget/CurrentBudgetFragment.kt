package com.s95ammar.budgetplanner.ui.currentbudget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.s95ammar.budgetplanner.R
import com.s95ammar.budgetplanner.ui.ViewPagerFragmentAdapter
import com.s95ammar.budgetplanner.ui.base.BaseFragment
import com.s95ammar.budgetplanner.ui.budget.dashboard.DashboardFragment
import com.s95ammar.budgetplanner.ui.budget.expenses.ExpensesFragment
import com.s95ammar.budgetplanner.ui.fragmentProvider
import kotlinx.android.synthetic.main.fragment_current_budget.*

class CurrentBudgetFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_current_budget, container, false)
    }

    override fun setUpViews() {
        pager.adapter = ViewPagerFragmentAdapter(
            this,
            listOf(
                fragmentProvider { DashboardFragment.newInstance() },
                fragmentProvider { ExpensesFragment.newInstance() }
            )
        )
        val titles = listOf(getString(R.string.dashboard), getString(R.string.expenses))
        TabLayoutMediator(tab_layout, pager) { tab, position ->
            tab.text = titles[position]
        }.attach()
    }
}