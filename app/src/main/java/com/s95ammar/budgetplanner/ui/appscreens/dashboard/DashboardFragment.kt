package com.s95ammar.budgetplanner.ui.appscreens.dashboard

import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.s95ammar.budgetplanner.MobileNavigationDirections
import com.s95ammar.budgetplanner.R
import com.s95ammar.budgetplanner.databinding.FragmentDashboardBinding
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.common.data.CategoryOfPeriod
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.common.data.PeriodSimple
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.data.CurrentPeriodHeaderBundle
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.data.DashboardFabState
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.data.IntDashboardFabType
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.pager.IntDashboardTab
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.pager.budget.OverviewFragment
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.pager.budgettransactions.BudgetTransactionsFragment
import com.s95ammar.budgetplanner.ui.common.Keys
import com.s95ammar.budgetplanner.ui.common.LoadingState
import com.s95ammar.budgetplanner.ui.common.ViewPagerFragmentAdapter
import com.s95ammar.budgetplanner.ui.common.viewbinding.BaseViewBinderFragment
import com.s95ammar.budgetplanner.util.FAB_VISIBILITY_ANIMATION_DURATION_MS
import com.s95ammar.budgetplanner.util.INVALID
import com.s95ammar.budgetplanner.util.lifecycleutil.observeEvent
import dagger.hilt.android.AndroidEntryPoint
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.data.DashboardUiEvent as UiEvent

@AndroidEntryPoint
class DashboardFragment : BaseViewBinderFragment<FragmentDashboardBinding>(R.layout.fragment_dashboard) {

    private val viewModel: DashboardViewModel by viewModels()
    private val sharedViewModel: DashboardSharedViewModel by hiltNavGraphViewModels(R.id.nested_navigation_dashboard)

    private val pagerOnPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            viewModel.onTabSelected(position)
        }
    }

    override fun initViewBinding(view: View): FragmentDashboardBinding {
        return FragmentDashboardBinding.bind(view)
    }

    override fun setUpViews() {
        super.setUpViews()
        binding.imageButtonArrowPrevious.setOnClickListener { viewModel.onPreviousPeriodClick() }
        binding.imageButtonArrowNext.setOnClickListener { viewModel.onNextPeriodClick() }
        binding.textViewPeriodName.setOnClickListener { viewModel.onPeriodNameClick() }
        binding.imageButtonAddPeriod.setOnClickListener { viewModel.onAddPeriodClick() }
    }

    override fun initObservers() {
        super.initObservers()
        viewModel.currentPeriodBundle.observe(viewLifecycleOwner) {
            setViewsToCurrentPeriodBundle(it)
            sharedViewModel.onPeriodChanged(it.period?.id)
        }
        viewModel.fabState.observe(viewLifecycleOwner) { setFabState(it) }
        viewModel.performUiEvent.observeEvent(viewLifecycleOwner) { performUiEvent(it) }
        viewModel.dashboardTabs.observe(viewLifecycleOwner) { setUpViewPager(it) }

        sharedViewModel.performUiEvent.observeEvent(viewLifecycleOwner) { performUiEvent(it) }
    }

    private fun setUpViewPager(tabs: List<Int>) {
        val fragmentsLazyList = tabs.mapNotNull { tab -> getTabFragmentLazy(tab) }
        val titles = tabs.mapNotNull { tab -> getTabTitle(tab) }

        binding.pager.adapter = ViewPagerFragmentAdapter(
            parentFragment = this,
            fragmentsLazyList = fragmentsLazyList
        )
        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            tab.text = titles[position]
        }.attach()
    }

    private fun setFabState(fabState: DashboardFabState) {

        binding.fab.hide()
        binding.fab.postDelayed({
            executeIfViewIsAvailable {
                when (fabState.type) {
                    IntDashboardFabType.FAB_NONE -> {
                    }
                    IntDashboardFabType.FAB_EDIT -> {
                        binding.fab.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_edit))
                        binding.fab.show()
                    }
                    IntDashboardFabType.FAB_ADD -> {
                        binding.fab.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_add))
                        binding.fab.show()
                    }
                }

                when (fabState.currentTab) {
                    IntDashboardTab.TAB_OVERVIEW -> binding.fab.setOnClickListener { viewModel.onEditSelectedPeriod() }
                    IntDashboardTab.TAB_BUDGET_TRANSACTIONS -> binding.fab.setOnClickListener { viewModel.onAddBudgetTransaction() }
                }
            }
        }, FAB_VISIBILITY_ANIMATION_DURATION_MS)
    }

    override fun onStart() {
        super.onStart()
        binding.pager.registerOnPageChangeCallback(pagerOnPageChangeCallback)
    }

    override fun onStop() {
        super.onStop()
        binding.pager.unregisterOnPageChangeCallback(pagerOnPageChangeCallback)
    }

    private fun getTabFragmentLazy(@IntDashboardTab tab: Int) = when (tab) {
        IntDashboardTab.TAB_OVERVIEW -> lazy { OverviewFragment.newInstance() }
        IntDashboardTab.TAB_BUDGET_TRANSACTIONS -> lazy { BudgetTransactionsFragment.newInstance() }
        else -> null
    }

    private fun getTabTitle(@IntDashboardTab tab: Int) = when (tab) {
        IntDashboardTab.TAB_OVERVIEW -> getString(R.string.overview)
        IntDashboardTab.TAB_BUDGET_TRANSACTIONS -> getString(R.string.expenses_and_income)
        else -> null
    }

    private fun setViewsToCurrentPeriodBundle(currentPeriodHeaderBundle: CurrentPeriodHeaderBundle) {
        binding.textViewPeriodName.text = currentPeriodHeaderBundle.period?.name
        binding.imageButtonArrowPrevious.isVisible = currentPeriodHeaderBundle.isPreviousAvailable
        binding.imageButtonArrowNext.isVisible = currentPeriodHeaderBundle.isNextAvailable
        binding.imageButtonAddPeriod.isGone = currentPeriodHeaderBundle.isNextAvailable

        binding.textViewPeriodName.isGone = currentPeriodHeaderBundle.period == null
        binding.pager.isGone = currentPeriodHeaderBundle.period == null
        binding.instructionsLayout.root.isVisible = currentPeriodHeaderBundle.period == null
        binding.instructionsLayout.messageTextView.text = getString(R.string.instruction_create_period)
    }

    private fun performUiEvent(uiEvent: UiEvent) {
        when (uiEvent) {
            is UiEvent.NavigateToPeriodsList -> navigateToPeriodsList()
            is UiEvent.NavigateToCreatePeriod -> navigateToCreatePeriod()
            is UiEvent.NavigateToCreateBudgetTransaction -> navigateToCreateBudgetTransaction(uiEvent.periodId)
            is UiEvent.NavigateToEditBudgetTransaction -> navigateToEditBudgetTransaction(uiEvent.periodId, uiEvent.budgetTransactionId)
            is UiEvent.NavigateToEditPeriod -> navigateToEditPeriod(uiEvent.period)
            is UiEvent.NavigateToBudgetTransactionsMap -> navigateToBudgetTransactionsMap(uiEvent.periodId)
            is UiEvent.DisplayLoadingState -> handleLoadingState(uiEvent.loadingState)
            is UiEvent.NavigateToCreateEditEstimate -> listenAndNavigateToEstimateCreateEdit(uiEvent.categoryOfPeriod)
        }
    }

    private fun handleLoadingState(loadingState: LoadingState) {
        when (loadingState) {
            is LoadingState.Cold,
            is LoadingState.Success -> hideLoading()
            is LoadingState.Loading -> showLoading()
            is LoadingState.Error -> {
                hideLoading()
                showErrorSnackbar(loadingState.throwable)
            }
        }
    }

    private fun navigateToPeriodsList() {
        navController.navigate(DashboardFragmentDirections.actionNestedNavigationDashboardToPeriodsFragment())
    }

    private fun navigateToCreatePeriod() {
        navController.navigate(DashboardFragmentDirections.actionNavigationDashboardToNestedPeriodCreateEdit(null))
    }

    private fun navigateToCreateBudgetTransaction(periodId: Int) {
        navController.navigate(
            DashboardFragmentDirections
                .actionNavigationDashboardToBudgetTransactionCreateEditFragment(
                    periodId = periodId,
                    budgetTransactionId = Int.INVALID
                )
        )
    }

    private fun navigateToEditBudgetTransaction(periodId: Int, budgetTransactionId: Int) {
        navController.navigate(
            DashboardFragmentDirections
                .actionNavigationDashboardToBudgetTransactionCreateEditFragment(
                    periodId = periodId,
                    budgetTransactionId = budgetTransactionId
                )
        )
    }

    private fun navigateToEditPeriod(period: PeriodSimple) {
        navController.navigate(DashboardFragmentDirections.actionNavigationDashboardToNestedPeriodCreateEdit(period))
    }

    private fun navigateToBudgetTransactionsMap(periodId: Int) {
        navController.navigate(
            DashboardFragmentDirections.actionNavigationDashboardToBudgetTransactionsMapFragment(periodId)
        )
    }

    private fun listenAndNavigateToEstimateCreateEdit(categoryOfPeriod: CategoryOfPeriod) {
        setFragmentResultListener(Keys.KEY_ESTIMATE_REQUEST) { _, bundle ->
            val estimateOrNull = bundle.getDouble(Keys.KEY_ESTIMATE, 0.0).takeUnless { it == 0.0 }
            sharedViewModel.onCategoryOfPeriodEstimateChanged(categoryOfPeriod, estimateOrNull)
        }
        navController.navigate(
            MobileNavigationDirections
                .actionGlobalPeriodCategoryEstimateCreateEditFragment(categoryOfPeriod)
        )
    }
}
