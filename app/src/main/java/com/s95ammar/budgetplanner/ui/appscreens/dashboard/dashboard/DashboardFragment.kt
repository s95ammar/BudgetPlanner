package com.s95ammar.budgetplanner.ui.appscreens.dashboard.dashboard

import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.s95ammar.budgetplanner.R
import com.s95ammar.budgetplanner.databinding.FragmentDashboardBinding
import com.s95ammar.budgetplanner.ui.appscreens.auth.common.LoadingState
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.budget.BudgetFragment
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.budget.data.PeriodRecordsNavigationBundle
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.dashboard.data.CurrentPeriodBundle
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.dashboard.data.DashboardUiEvent
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
        binding.textViewPeriodName.setOnClickListener { viewModel.onPeriodNameClick() }
        binding.imageButtonAddPeriod.setOnClickListener { viewModel.onAddPeriodClick() }
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
        viewModel.performUiEvent.observeEvent(viewLifecycleOwner) { performUiEvent(it) }
        sharedViewModel.onNavigateToPeriodRecords.observeEvent(viewLifecycleOwner) { onNavigateToPeriodRecords(it) }
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

    private fun performUiEvent(uiEvent: DashboardUiEvent) {
        when (uiEvent) {
            is DashboardUiEvent.NavigateToPeriodsList -> { onNavigateToPeriodsList() }
            is DashboardUiEvent.NavigateToCreatePeriod -> { onNavigateToCreatePeriod() }
            is DashboardUiEvent.DisplayLoadingState -> { handleLoadingState(uiEvent.loadingState) }
        }
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

    private fun onNavigateToPeriodsList() {
        listenToPeriodsListChangedResult()
        navController.navigate(DashboardFragmentDirections.actionNestedNavigationDashboardToPeriodsFragment())
    }

    private fun onNavigateToCreatePeriod() {
        listenToPeriodsListChangedResult()
        navController.navigate(DashboardFragmentDirections.actionNestedNavigationDashboardToPeriodCreateEditFragment(Int.NO_ITEM))
    }

    private fun onNavigateToPeriodRecords(navigationBundle: PeriodRecordsNavigationBundle) {
        setFragmentResultListener(Keys.KEY_ON_PERIOD_RECORD_ADDED) { _, _ -> sharedViewModel.onPeriodRecordAdded() }
        navController.navigate(
            DashboardFragmentDirections.actionNavigationDashboardToPeriodRecordsFragment(
                navigationBundle.excludedCategoryIds.toIntArray(),
                navigationBundle.periodId
            )
        )
    }

    private fun listenToPeriodsListChangedResult() {
        setFragmentResultListener(Keys.KEY_ON_PERIODS_LIST_CHANGED) { _, _ -> viewModel.refresh() }
    }

}