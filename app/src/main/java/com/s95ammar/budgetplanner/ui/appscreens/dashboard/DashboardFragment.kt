package com.s95ammar.budgetplanner.ui.appscreens.dashboard

import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.s95ammar.budgetplanner.R
import com.s95ammar.budgetplanner.databinding.FragmentDashboardBinding
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.childscreens.budget.BudgetFragment
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.childscreens.budgettransactions.BudgetTransactionsFragment
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.childscreens.savings.SavingsFragment
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.data.CurrentPeriodHeaderBundle
import com.s95ammar.budgetplanner.ui.common.IntLoadingType
import com.s95ammar.budgetplanner.ui.common.Keys
import com.s95ammar.budgetplanner.ui.common.LoadingState
import com.s95ammar.budgetplanner.ui.common.viewbinding.BaseViewBinderFragment
import com.s95ammar.budgetplanner.ui.common.viewpagerhelpers.FragmentProvider
import com.s95ammar.budgetplanner.ui.common.viewpagerhelpers.ViewPagerFragmentAdapter
import com.s95ammar.budgetplanner.util.NO_ITEM
import com.s95ammar.budgetplanner.util.lifecycleutil.observeEvent
import dagger.hilt.android.AndroidEntryPoint
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.data.DashboardUiEvent as UiEvent

@AndroidEntryPoint
class DashboardFragment : BaseViewBinderFragment<FragmentDashboardBinding>(R.layout.fragment_dashboard) {

    private val viewModel: DashboardViewModel by viewModels()
    private val sharedViewModel: DashboardSharedViewModel by hiltNavGraphViewModels(R.id.nested_navigation_dashboard)

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
            parentFragment = this,
            childFragmentsProviders = listOf(
                FragmentProvider { BudgetFragment.newInstance() },
                FragmentProvider { BudgetTransactionsFragment.newInstance() },
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
            setViewsToCurrentPeriodBundle(it)
//            sharedViewModel.onPeriodChanged(it.period?.id) // TODO
        }
        viewModel.performUiEvent.observeEvent(viewLifecycleOwner) { performUiEvent(it) }
        sharedViewModel.performDashboardUiEvent.observeEvent(viewLifecycleOwner) { performUiEvent(it) }
    }

    private fun setViewsToCurrentPeriodBundle(currentPeriodHeaderBundle: CurrentPeriodHeaderBundle) {
        binding.textViewPeriodName.text = currentPeriodHeaderBundle.period?.name
        binding.imageButtonArrowPrevious.isVisible = currentPeriodHeaderBundle.isPreviousAvailable
        binding.imageButtonArrowNext.isVisible = currentPeriodHeaderBundle.isNextAvailable
        binding.imageButtonAddPeriod.isGone = currentPeriodHeaderBundle.isNextAvailable

        binding.pager.isGone = currentPeriodHeaderBundle.period == null
        binding.instructionsLayout.root.isVisible = currentPeriodHeaderBundle.period == null
        binding.instructionsLayout.messageTextView.text = getString(R.string.create_period_instruction)
    }

    private fun performUiEvent(uiEvent: UiEvent) {
        when (uiEvent) {
            is UiEvent.NavigateToPeriodsList -> onNavigateToPeriodsList()
            is UiEvent.NavigateToCreatePeriod -> onNavigateToCreatePeriod()
            is UiEvent.NavigateToCreateBudgetTransaction -> navigateToCreateBudgetTransaction(uiEvent.periodId)
            is UiEvent.NavigateToEditBudgetTransaction -> navigateToEditBudgetTransaction(uiEvent.periodId, uiEvent.budgetTransactionId)
            is UiEvent.NavigateToEditPeriod -> navigateToEditPeriod(uiEvent.periodId)
            is UiEvent.DisplayLoadingState -> handleLoadingState(uiEvent.loadingState, uiEvent.loadingType)
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

    private fun onNavigateToPeriodsList() {
        listenToPeriodsListChangedResult()
        navController.navigate(DashboardFragmentDirections.actionNestedNavigationDashboardToPeriodsFragment())
    }

    private fun onNavigateToCreatePeriod() {
        listenToPeriodsListChangedResult()
        navController.navigate(DashboardFragmentDirections.actionNavigationDashboardToNestedPeriodCreateEdit(Int.NO_ITEM))
    }

    private fun navigateToCreateBudgetTransaction(periodId: Int) {
//        listenToBudgetTransactionsListChangedResult()
        navController.navigate(
            DashboardFragmentDirections
                .actionNavigationDashboardToBudgetTransactionCreateEditFragment(
                    periodId = periodId,
                    budgetTransactionId = Int.NO_ITEM
                )
        )
    }

    private fun navigateToEditBudgetTransaction(periodId: Int, budgetTransactionId: Int) {
//        listenToBudgetTransactionsListChangedResult()
        navController.navigate(
            DashboardFragmentDirections
                .actionNavigationDashboardToBudgetTransactionCreateEditFragment(
                    periodId = periodId,
                    budgetTransactionId = Int.NO_ITEM
                )
        )
    }

    private fun navigateToEditPeriod(periodId: Int) {
//        setFragmentResultListener(Keys.KEY_DASHBOARD_SCREEN_ON_PERIODS_LIST_CHANGED) { _, _ -> sharedViewModel.onPeriodicCategoriesChanged() }
        navController.navigate(DashboardFragmentDirections.actionNavigationDashboardToNestedPeriodCreateEdit(periodId))
    }

    private fun listenToPeriodsListChangedResult() {
        setFragmentResultListener(Keys.KEY_PERIODIC_CATEGORIES_SCREEN_ON_PERIODS_LIST_CHANGED) { _, _ -> viewModel.refresh() }
    }

}