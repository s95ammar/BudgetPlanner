package com.s95ammar.budgetplanner.ui.appscreens.dashboard.budget

import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.s95ammar.budgetplanner.R
import com.s95ammar.budgetplanner.databinding.FragmentDashboardBudgetBinding
import com.s95ammar.budgetplanner.models.api.responses.errors.ForbiddenError
import com.s95ammar.budgetplanner.models.api.responses.errors.NotFoundError
import com.s95ammar.budgetplanner.ui.appscreens.auth.common.LoadingState
import com.s95ammar.budgetplanner.ui.appscreens.auth.login.validation.LoginValidator
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.DashboardFragmentDirections
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.budget.data.CurrentPeriodBundle
import com.s95ammar.budgetplanner.ui.base.BaseFragment
import com.s95ammar.budgetplanner.ui.common.Keys
import com.s95ammar.budgetplanner.ui.common.viewbinding.ViewBinder
import com.s95ammar.budgetplanner.util.NO_ITEM
import com.s95ammar.budgetplanner.util.lifecycleutil.observeEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BudgetFragment : BaseFragment(R.layout.fragment_dashboard_budget), ViewBinder<FragmentDashboardBudgetBinding> {

    companion object {
        fun newInstance() = BudgetFragment()
    }

    private val viewModel: BudgetViewModel by viewModels()

    override val binding: FragmentDashboardBudgetBinding
        get() = getBinding()

    override fun initViewBinding(view: View): FragmentDashboardBudgetBinding {
        return FragmentDashboardBudgetBinding.bind(view)
    }

    override fun setUpViews() {
        super.setUpViews()
        binding.imageButtonArrowPrevious.setOnClickListener { viewModel.onPreviousPeriodClick() }
        binding.imageButtonArrowNext.setOnClickListener { viewModel.onNextPeriodClick() }
        binding.textViewPeriodName.setOnClickListener { navigateToPeriodsList() }
        binding.imageButtonAddPeriod.setOnClickListener { navigateToCreatePeriod() }
    }

    override fun initObservers() {
        super.initObservers()
        viewModel.currentPeriodBundle.observe(viewLifecycleOwner) { setViewsToCurrentPeriodBundle(it) }
        viewModel.displayLoadingState.observeEvent(viewLifecycleOwner) { handleLoadingState(it) }
        observeResultLiveData<Boolean>(Keys.KEY_ON_PERIOD_CREATE_EDIT) { viewModel.refresh() }
    }

    private fun setViewsToCurrentPeriodBundle(currentPeriodBundle: CurrentPeriodBundle) {
        binding.textViewPeriodName.text = currentPeriodBundle.period.name
        binding.imageButtonArrowPrevious.isVisible = currentPeriodBundle.isPreviousAvailable
        binding.imageButtonArrowNext.isVisible = currentPeriodBundle.isNextAvailable
        binding.imageButtonAddPeriod.isGone = currentPeriodBundle.isNextAvailable
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

    // TODO: refactor using nested nav graph or shared viewModel
    private fun navigateToPeriodsList() {
        requireParentFragment()
            .findNavController()
            .navigate(DashboardFragmentDirections.actionNavigationDashboardToPeriodsFragment())

    }

    // TODO: refactor using nested nav graph or shared viewModel
    private fun navigateToCreatePeriod() {
        requireParentFragment()
            .findNavController()
            .navigate(DashboardFragmentDirections.actionNavigationDashboardToPeriodCreateEditFragment(Int.NO_ITEM))
    }
}