package com.s95ammar.budgetplanner.ui.appscreens.dashboard.childscreens.budgettransactions

import android.view.View
import androidx.fragment.app.viewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import com.s95ammar.budgetplanner.R
import com.s95ammar.budgetplanner.databinding.FragmentDashboardTransactionsBinding
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.DashboardSharedViewModel
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.childscreens.budgettransactions.adapter.BudgetTransactionsListAdapter
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.common.data.BudgetTransactionViewEntity
import com.s95ammar.budgetplanner.ui.base.BaseFragment
import com.s95ammar.budgetplanner.ui.common.viewbinding.ViewBinder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BudgetTransactionsFragment : BaseFragment(R.layout.fragment_dashboard_transactions), ViewBinder<FragmentDashboardTransactionsBinding> {

    companion object {
        fun newInstance() = BudgetTransactionsFragment()
    }

    private val viewModel: BudgetTransactionsViewModel by viewModels()
    private val sharedViewModel: DashboardSharedViewModel by hiltNavGraphViewModels(R.id.nested_navigation_dashboard)

    private val adapter by lazy { BudgetTransactionsListAdapter() }

    override val binding: FragmentDashboardTransactionsBinding
        get() = getBinding()

    override fun initViewBinding(view: View): FragmentDashboardTransactionsBinding {
        return FragmentDashboardTransactionsBinding.bind(view)
    }

    override fun setUpViews() {
        super.setUpViews()
//        binding.fab.setOnClickListener { sharedViewModel.onAddBudgetTransaction() } // TODO
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        binding.recyclerView.adapter = adapter
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
    }

    override fun initObservers() {
        super.initObservers()
        sharedViewModel.isPeriodAvailable.observe(viewLifecycleOwner) { setFabVisibility(it) }
        sharedViewModel.currentBudgetTransactions.observe(viewLifecycleOwner) { setBudgetTransactionViewEntity(it) }
    }

    private fun setFabVisibility(isVisible: Boolean) {
        if (isVisible) binding.fab.show() else binding.fab.hide()
    }

    private fun setBudgetTransactionViewEntity(budgetTransactions: List<BudgetTransactionViewEntity>) {
        adapter.submitList(budgetTransactions)
    }

}