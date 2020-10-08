package com.s95ammar.budgetplanner.ui.budgetslist

import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.s95ammar.budgetplanner.R
import com.s95ammar.budgetplanner.models.Resource
import com.s95ammar.budgetplanner.ui.base.BaseFragment
import com.s95ammar.budgetplanner.ui.budgetslist.adapter.BudgetsListAdapter
import com.s95ammar.budgetplanner.ui.budgetslist.entity.BudgetViewEntity
import com.s95ammar.budgetplanner.ui.common.Keys
import com.s95ammar.budgetplanner.util.NO_ITEM
import com.s95ammar.budgetplanner.util.lifecycleutil.observeEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_budgets_list.*

@AndroidEntryPoint
class BudgetsListFragment : BaseFragment(R.layout.fragment_budgets_list) {

    private val viewModel: BudgetsListViewModel by viewModels()
    private val adapter by lazy { BudgetsListAdapter(viewModel::onBudgetItemClick) }

    override fun setUpViews() {
        super.setUpViews()
        fab_budgets_list.setOnClickListener { navigateToCreateEditBudget(Int.NO_ITEM) }
        recycler_view_budgets_list.setHasFixedSize(true)
        recycler_view_budgets_list.adapter = adapter
    }

    override fun initObservers() {
        super.initObservers()
        viewModel.allBudgets.observe(viewLifecycleOwner) { handleAllBudgetsLoading(it) }
        viewModel.navigateToEditBudget.observeEvent(viewLifecycleOwner) { navigateToCreateEditBudget(it) }

        observeResult<Int>(Keys.KEY_RESULT_ACTIVE_BUDGET_CHANGED) { id -> viewModel.onActiveBudgetChanged(id) }
    }

    private fun handleAllBudgetsLoading(allBudgetsResource: Resource<List<BudgetViewEntity>>?) {
        when (allBudgetsResource) {
            is Resource.Loading -> loadingManager?.showLoading()
            is Resource.Error -> {
                loadingManager?.hideLoading()
                displayError(allBudgetsResource.throwable)
            }
            is Resource.Success -> {
                loadingManager?.hideLoading()
                adapter.submitList(allBudgetsResource.data)
            }
        }
    }

    private fun navigateToCreateEditBudget(budgetId: Int) {
        navController.navigate(
            R.id.action_navigation_budgets_list_to_budgetCreateEditFragment,
            bundleOf(Keys.KEY_BUDGET_ID to budgetId)
        )
    }
}