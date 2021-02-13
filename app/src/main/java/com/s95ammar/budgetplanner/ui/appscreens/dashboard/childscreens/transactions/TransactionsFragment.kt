package com.s95ammar.budgetplanner.ui.appscreens.dashboard.childscreens.transactions

import android.view.View
import androidx.fragment.app.viewModels
import com.s95ammar.budgetplanner.R
import com.s95ammar.budgetplanner.databinding.FragmentDashboardTransactionsBinding
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.DashboardSharedViewModel
import com.s95ammar.budgetplanner.ui.base.BaseFragment
import com.s95ammar.budgetplanner.ui.common.viewbinding.ViewBinder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TransactionsFragment : BaseFragment(R.layout.fragment_dashboard_transactions), ViewBinder<FragmentDashboardTransactionsBinding> {

    companion object {
        fun newInstance() = TransactionsFragment()
    }

    private val viewModel: TransactionsViewModel by viewModels()
    private val sharedViewModel: DashboardSharedViewModel by viewModels(ownerProducer = { requireParentFragment() })

    override val binding: FragmentDashboardTransactionsBinding
        get() = getBinding()

    override fun initViewBinding(view: View): FragmentDashboardTransactionsBinding {
        return FragmentDashboardTransactionsBinding.bind(view)
    }

    override fun setUpViews() {
        super.setUpViews()
    }

    override fun initObservers() {
        super.initObservers()
        sharedViewModel.selectedPeriodId.observe(viewLifecycleOwner) { viewModel.onPeriodChanged(it) }
    }

}