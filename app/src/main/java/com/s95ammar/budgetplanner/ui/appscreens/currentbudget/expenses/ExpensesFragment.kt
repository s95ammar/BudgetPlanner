package com.s95ammar.budgetplanner.ui.appscreens.currentbudget.expenses

import androidx.fragment.app.viewModels
import com.s95ammar.budgetplanner.R
import com.s95ammar.budgetplanner.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExpensesFragment : BaseFragment(R.layout.fragment_budget_expenses) {

    companion object {
        fun newInstance() = ExpensesFragment()
    }

    private val viewModel: ExpensesViewModel by viewModels()

}