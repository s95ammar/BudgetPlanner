package com.s95ammar.budgetplanner.ui.budget.dashboard.savings

import androidx.fragment.app.viewModels
import com.s95ammar.budgetplanner.R
import com.s95ammar.budgetplanner.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SavingsFragment : BaseFragment(R.layout.fragment_budget_savings) {

    companion object {
        fun newInstance() = SavingsFragment()
    }

    private val viewModel: SavingsViewModel by viewModels()

}