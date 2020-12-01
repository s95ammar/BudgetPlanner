package com.s95ammar.budgetplanner.ui.appscreens.dashboard.budget

import android.view.View
import androidx.fragment.app.viewModels
import com.s95ammar.budgetplanner.R
import com.s95ammar.budgetplanner.databinding.FragmentDashboardBudgetBinding
import com.s95ammar.budgetplanner.ui.appscreens.auth.common.LoadingState
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.DashboardSharedViewModel
import com.s95ammar.budgetplanner.ui.base.BaseFragment
import com.s95ammar.budgetplanner.ui.common.viewbinding.ViewBinder
import com.s95ammar.budgetplanner.util.lifecycleutil.observeEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BudgetFragment : BaseFragment(R.layout.fragment_dashboard_budget), ViewBinder<FragmentDashboardBudgetBinding> {

    companion object {
        fun newInstance() = BudgetFragment()
    }

    private val viewModel: BudgetViewModel by viewModels()
    private val sharedViewModel: DashboardSharedViewModel by viewModels(ownerProducer = { requireParentFragment() })

    override val binding: FragmentDashboardBudgetBinding
        get() = getBinding()

    override fun initViewBinding(view: View): FragmentDashboardBudgetBinding {
        return FragmentDashboardBudgetBinding.bind(view)
    }

    override fun setUpViews() {
        super.setUpViews()
    }

    override fun initObservers() {
        super.initObservers()
        viewModel.periodRecords
        viewModel.displayLoadingState.observeEvent(viewLifecycleOwner) { handleLoadingState(it) }

        sharedViewModel.selectedPeriodId.observe(viewLifecycleOwner) { viewModel.onPeriodChanged(it) }
    }

    private fun handleLoadingState(loadingState: LoadingState) {
        when (loadingState) {
            is LoadingState.Cold,
            is LoadingState.Success -> hideLoading()
            is LoadingState.Loading -> showLoading()
            is LoadingState.Error -> {
                hideLoading()
                handleError(loadingState.throwable)
            }
        }
    }

    private fun handleError(throwable: Throwable) {
        when (throwable) {
            else -> throwable.message?.let { showToast(it) }
        }
    }
}