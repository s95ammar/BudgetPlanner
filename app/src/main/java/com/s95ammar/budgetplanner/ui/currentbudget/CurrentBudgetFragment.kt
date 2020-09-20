package com.s95ammar.budgetplanner.ui.currentbudget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.s95ammar.budgetplanner.R
import com.s95ammar.budgetplanner.ui.ViewPagerFragmentAdapter
import com.s95ammar.budgetplanner.ui.base.BaseFragment
import com.s95ammar.budgetplanner.ui.budget.dashboard.DashboardFragment
import com.s95ammar.budgetplanner.ui.budget.dashboard.savings.SavingsFragment
import com.s95ammar.budgetplanner.ui.budget.expenses.ExpensesFragment
import com.s95ammar.budgetplanner.ui.fragmentProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_current_budget.*

@AndroidEntryPoint
class CurrentBudgetFragment : BaseFragment() {

    private val viewModel: CurrentBudgetViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_current_budget, container, false)
    }

    override fun setUpViews() {
        super.setUpViews()
        pager.adapter = ViewPagerFragmentAdapter(
            this,
            listOf(
                fragmentProvider { DashboardFragment.newInstance() },
                fragmentProvider { ExpensesFragment.newInstance() },
                fragmentProvider { SavingsFragment.newInstance() }
            )
        )
        val titles = listOf(
            getString(R.string.title_dashboard),
            getString(R.string.title_expenses),
            getString(R.string.title_savings)
        )
        TabLayoutMediator(tab_layout, pager) { tab, position ->
            tab.text = titles[position]
        }.attach()
    }
}