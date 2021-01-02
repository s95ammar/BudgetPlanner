package com.s95ammar.budgetplanner.ui.appscreens.dashboard

import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.s95ammar.budgetplanner.R
import com.s95ammar.budgetplanner.databinding.FragmentDashboardBinding
import com.s95ammar.budgetplanner.ui.appscreens.auth.common.LoadingState
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.budget.BudgetFragment
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.budget.data.PeriodRecordsNavigationBundle
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.data.CurrentPeriodBundle
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.savings.SavingsFragment
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.transactions.TransactionsFragment
import com.s95ammar.budgetplanner.ui.base.BaseFragment
import com.s95ammar.budgetplanner.ui.common.Keys
import com.s95ammar.budgetplanner.ui.common.viewbinding.ViewBinder
import com.s95ammar.budgetplanner.ui.common.viewpagerhelpers.FragmentProvider
import com.s95ammar.budgetplanner.ui.common.viewpagerhelpers.ViewPagerFragmentAdapter
import com.s95ammar.budgetplanner.util.NO_ITEM
import com.s95ammar.budgetplanner.util.lifecycleutil.hiltNavGraphViewModels
import com.s95ammar.budgetplanner.util.lifecycleutil.observeEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardFragment : BaseFragment(R.layout.fragment_dashboard), ViewBinder<FragmentDashboardBinding> {

    private val viewModel: DashboardViewModel by viewModels()
    private val sharedViewModel: DashboardSharedViewModel by hiltNavGraphViewModels(R.id.nested_navigation_dashboard)

    override val binding: FragmentDashboardBinding
        get() = getBinding()

    override fun initViewBinding(view: View): FragmentDashboardBinding {
        return FragmentDashboardBinding.bind(view)
    }

    override fun setUpViews() {
        super.setUpViews()
        setUpViewPager()
        binding.imageButtonArrowPrevious.setOnClickListener { viewModel.onPreviousPeriodClick() }
        binding.imageButtonArrowNext.setOnClickListener { viewModel.onNextPeriodClick() }
        binding.textViewPeriodName.setOnClickListener { navigateToPeriodsList() }
        binding.imageButtonAddPeriod.setOnClickListener { navigateToCreatePeriod() }
    }

    private fun setUpViewPager() {
        binding.pager.adapter = ViewPagerFragmentAdapter(
            this,
            listOf(
                FragmentProvider { BudgetFragment.newInstance() },
                FragmentProvider { TransactionsFragment.newInstance() },
                FragmentProvider { SavingsFragment.newInstance() }
            )
        )
        val titles = listOf(
            getString(R.string.budget),
            getString(R.string.expenses_and_income),
            getString(R.string.savings)
        )
        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            tab.text = titles[position]
        }.attach()
    }

    override fun initObservers() {
        super.initObservers()
        viewModel.currentPeriodBundle.observe(viewLifecycleOwner) { onCurrentPeriodChanged(it) }
        viewModel.displayLoadingState.observeEvent(viewLifecycleOwner) { handleLoadingState(it) }
        sharedViewModel.navigateToPeriodRecords.observeEvent(viewLifecycleOwner) { navigateToPeriodRecords(it) }
        observeResultLiveData<Boolean>(Keys.KEY_ON_PERIOD_CREATE_EDIT) { viewModel.refresh() }
    }

    private fun onCurrentPeriodChanged(currentPeriodBundle: CurrentPeriodBundle) {
        currentPeriodBundle.period?.id?.let { sharedViewModel.onPeriodChanged(it) }
        setViewsToCurrentPeriodBundle(currentPeriodBundle)
    }

    private fun setViewsToCurrentPeriodBundle(currentPeriodBundle: CurrentPeriodBundle) {
        binding.textViewPeriodName.text = currentPeriodBundle.period?.name
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

    private fun navigateToPeriodsList() {
        navController.navigate(DashboardFragmentDirections.actionNestedNavigationDashboardToPeriodsFragment())

    }

    private fun navigateToCreatePeriod() {
        navController.navigate(DashboardFragmentDirections.actionNestedNavigationDashboardToPeriodCreateEditFragment(Int.NO_ITEM))
    }

    private fun navigateToPeriodRecords(navigationBundle: PeriodRecordsNavigationBundle) {
        navController
            .navigate(
                DashboardFragmentDirections.actionNavigationDashboardToPeriodRecordsFragment(
                    navigationBundle.excludedCategoryIds.toIntArray(),
                    navigationBundle.periodId
                )
            )
    }

}