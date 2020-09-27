package com.s95ammar.budgetplanner.ui.budgetslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.s95ammar.budgetplanner.R
import com.s95ammar.budgetplanner.ui.base.BaseFragment
import com.s95ammar.budgetplanner.ui.base.BaseListAdapter
import com.s95ammar.budgetplanner.ui.budgetslist.adapter.BudgetsListAdapter
import com.s95ammar.budgetplanner.ui.common.BundleKey
import com.s95ammar.budgetplanner.ui.common.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_budgets_list.*

@AndroidEntryPoint
class BudgetsListFragment : BaseFragment() {

    private val viewModel: BudgetsListViewModel by viewModels()
    private val adapter by lazy { BudgetsListAdapter(viewModel::onBudgetItemClick) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_budgets_list, container, false)
    }

    override fun setUpViews() {
        super.setUpViews()
        fab_budgets_list.setOnClickListener { navigateToCreateEditBudget(Constants.NO_ITEM) }
        recycler_view_budgets_list.adapter = adapter
    }

    override fun initObservers() {
        super.initObservers()
        viewModel.getAllBudgets().observe(viewLifecycleOwner) { adapter.submitList(it) }
    }

    private fun navigateToCreateEditBudget(budgetId: Int) {
        findNavController().navigate(
            R.id.action_navigation_budgets_list_to_budgetCreateEditFragment,
            bundleOf(
                BundleKey.KEY_BUDGET_ID to budgetId
            )
        )
    }
}