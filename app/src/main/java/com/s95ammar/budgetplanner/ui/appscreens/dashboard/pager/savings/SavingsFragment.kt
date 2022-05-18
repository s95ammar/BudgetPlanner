package com.s95ammar.budgetplanner.ui.appscreens.dashboard.pager.savings

import androidx.fragment.app.viewModels
import com.s95ammar.budgetplanner.R
import com.s95ammar.budgetplanner.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SavingsFragment : BaseFragment(R.layout.fragment_dashboard_savings) {

    companion object {
        fun newInstance() = SavingsFragment()
    }

    private val viewModel: SavingsViewModel by viewModels()

}