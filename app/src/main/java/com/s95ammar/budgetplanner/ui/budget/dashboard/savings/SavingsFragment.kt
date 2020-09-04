package com.s95ammar.budgetplanner.ui.budget.dashboard.savings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.s95ammar.budgetplanner.R
import com.s95ammar.budgetplanner.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SavingsFragment : BaseFragment() {

    companion object {
        fun newInstance() = SavingsFragment()
    }

    private val viewModel: SavingsViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_budget_savings, container, false)
    }

}