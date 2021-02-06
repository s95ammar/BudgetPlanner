package com.s95ammar.budgetplanner.ui.appscreens.dashboard

import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.s95ammar.budgetplanner.R
import com.s95ammar.budgetplanner.databinding.FragmentDashboardBinding
import com.s95ammar.budgetplanner.ui.appscreens.auth.common.LoadingState
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.data.CurrentPeriodHeaderBundle
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.data.DashboardUiEvent
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budget.BudgetFragment
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.savings.SavingsFragment
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.transactions.TransactionsFragment
import com.s95ammar.budgetplanner.ui.base.BaseFragment
import com.s95ammar.budgetplanner.ui.common.IntLoadingType
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
        binding.swipeToRefreshLayout.setOnRefreshListener { sharedViewModel.onRefresh() }
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
        viewModel.currentPeriodBundle.observe(viewLifecycleOwner) {
            sharedViewModel.onPeriodChanged(it.period?.id)
            setViewsToCurrentPeriodBundle(it)
        }
        viewModel.performUiEvent.observeEvent(viewLifecycleOwner) { performUiEvent(it) }
        sharedViewModel.performDashboardUiEvent.observeEvent(viewLifecycleOwner) { performUiEvent(it) }
    }

    private fun setViewsToCurrentPeriodBundle(currentPeriodHeaderBundle: CurrentPeriodHeaderBundle) {
        binding.textViewPeriodName.text = currentPeriodHeaderBundle.period?.name
        binding.imageButtonArrowPrevious.isVisible = currentPeriodHeaderBundle.isPreviousAvailable
        binding.imageButtonArrowNext.isVisible = currentPeriodHeaderBundle.isNextAvailable
        binding.imageButtonAddPeriod.isGone = currentPeriodHeaderBundle.isNextAvailable
    }

    private fun performUiEvent(uiEvent: DashboardUiEvent) {
        when (uiEvent) {
            is DashboardUiEvent.NavigateToPeriodsList -> navigateToPeriodsList()
            is DashboardUiEvent.NavigateToCreatePeriod -> navigateToCreatePeriod()
            is DashboardUiEvent.NavigateToEditPeriod -> navigateToEditPeriod(uiEvent.periodId)
            is DashboardUiEvent.DisplayLoadingState -> handleLoadingState(uiEvent.loadingState, uiEvent.loadingType)
        }
    }

    private fun handleLoadingState(loadingState: LoadingState, @IntLoadingType loadingType: Int) {
        when (loadingState) {
            is LoadingState.Cold,
            is LoadingState.Success -> hideLoading(loadingType)
            is LoadingState.Loading -> showLoading(loadingType)
            is LoadingState.Error -> {
                hideLoading(loadingType)
                handleError(loadingState.throwable)
            }
        }
    }

    private fun hideLoading(@IntLoadingType loadingType: Int) {
        when (loadingType) {
            IntLoadingType.SWIPE_TO_REFRESH -> binding.swipeToRefreshLayout.isRefreshing = false
            IntLoadingType.MAIN -> hideLoading()
        }
    }

    private fun showLoading(@IntLoadingType loadingType: Int) {
        when (loadingType) {
            IntLoadingType.SWIPE_TO_REFRESH -> binding.swipeToRefreshLayout.isRefreshing = true
            IntLoadingType.MAIN -> showLoading()
        }
    }

    private fun handleError(throwable: Throwable) {
        when (throwable) {
            else -> showErrorToast(throwable)
        }
    }

    private fun navigateToPeriodsList() {
        listenToPeriodsListChangedResult()
        navController.navigate(DashboardFragmentDirections.actionNestedNavigationDashboardToPeriodsFragment())
    }

    private fun navigateToCreatePeriod() {
        listenToPeriodsListChangedResult()
        navController.navigate(DashboardFragmentDirections.actionNestedNavigationDashboardToPeriodCreateEditFragment(Int.NO_ITEM))
    }

    private fun navigateToEditPeriod(periodId: Int) {
//        setFragmentResultListener(Keys.KEY_DASHBOARD_SCREEN_ON_PERIODS_LIST_CHANGED) { _, _ -> sharedViewModel.onPeriodicCategoriesChanged() }
        navController.navigate(DashboardFragmentDirections.actionNestedNavigationDashboardToPeriodCreateEditFragment(periodId))
    }

    private fun listenToPeriodsListChangedResult() {
        setFragmentResultListener(Keys.KEY_PERIODIC_CATEGORIES_SCREEN_ON_PERIODS_LIST_CHANGED) { _, _ -> viewModel.refresh() }
    }

}