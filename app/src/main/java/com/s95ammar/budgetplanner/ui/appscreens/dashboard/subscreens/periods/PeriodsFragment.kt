package com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.periods

import android.os.Bundle
import android.view.View
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import com.s95ammar.budgetplanner.R
import com.s95ammar.budgetplanner.databinding.FragmentPeriodsBinding
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.common.data.PeriodSimple
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.periods.adapter.PeriodsListAdapter
import com.s95ammar.budgetplanner.ui.base.BaseFragment
import com.s95ammar.budgetplanner.ui.common.Keys
import com.s95ammar.budgetplanner.ui.common.LoadingState
import com.s95ammar.budgetplanner.ui.common.bottomsheet.EditDeleteBottomSheetDialogFragment
import com.s95ammar.budgetplanner.ui.common.viewbinding.ViewBinder
import com.s95ammar.budgetplanner.util.lifecycleutil.observeEvent
import dagger.hilt.android.AndroidEntryPoint
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.periods.data.PeriodsUiEvent as UiEvent

@AndroidEntryPoint
class PeriodsFragment : BaseFragment(R.layout.fragment_periods), ViewBinder<FragmentPeriodsBinding> {

    override val binding: FragmentPeriodsBinding
        get() = getBinding()

    private val viewModel: PeriodsViewModel by viewModels()
    private val adapter by lazy { PeriodsListAdapter(viewModel::onPeriodItemClick, viewModel::onPeriodItemLongClick) }

    override fun initViewBinding(view: View): FragmentPeriodsBinding {
        return FragmentPeriodsBinding.bind(view)
    }

    override fun setUpViews() {
        super.setUpViews()
        binding.toolbar.setNavigationOnClickListener { navController.navigateUp() }
        binding.swipeToRefreshLayout.setOnRefreshListener { viewModel.refresh() }
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        binding.recyclerView.adapter = adapter
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
    }

    override fun initObservers() {
        super.initObservers()
        viewModel.allPeriods.observe(viewLifecycleOwner) { setAllPeriods(it) }
        viewModel.performUiEvent.observeEvent(viewLifecycleOwner) { performUiEvent(it) }
    }

    override fun showLoading() {
        binding.swipeToRefreshLayout.isRefreshing = true
    }

    override fun hideLoading() {
        binding.swipeToRefreshLayout.isRefreshing = false
    }

    private fun setAllPeriods(periods: List<PeriodSimple>) {
        adapter.submitList(periods)
    }

    private fun performUiEvent(uiEvent: UiEvent) {
        when (uiEvent) {
            is UiEvent.DisplayLoadingState -> handleLoadingState(uiEvent.loadingState)
            is UiEvent.OnNavigateToEditPeriod -> onNavigateToCreateEditPeriod(uiEvent.period)
            is UiEvent.ShowBottomSheet -> showBottomSheet(uiEvent.period)
            is UiEvent.OnPeriodDeleted -> onPeriodDeleted()
            is UiEvent.Exit -> navController.navigateUp()
        }
    }

    private fun handleLoadingState(loadingState: LoadingState) {
        when (loadingState) {
            is LoadingState.Cold,
            is LoadingState.Success -> hideLoading()
            is LoadingState.Loading -> showLoading()
            is LoadingState.Error -> {
                hideLoading()
                showErrorToast(loadingState.throwable)
            }
        }
    }

    private fun onNavigateToCreateEditPeriod(period: PeriodSimple) {
        setFragmentResultListener(Keys.KEY_ON_PERIOD_CREATE_EDIT) { _, _ -> viewModel.refresh() }
        navController.navigate(
            PeriodsFragmentDirections.actionPeriodsFragmentToNestedPeriodCreateEdit(period.id, period.name)
        )
    }

    private fun showBottomSheet(period: PeriodSimple) {
        EditDeleteBottomSheetDialogFragment.newInstance(period.name, R.drawable.ic_period).apply {
            listener = object : EditDeleteBottomSheetDialogFragment.Listener {
                override fun onEdit() = onNavigateToCreateEditPeriod(period)
                override fun onDelete() = displayDeleteConfirmationDialog(period.name) { viewModel.deletePeriod(period.id) }
            }
        }.show(childFragmentManager, EditDeleteBottomSheetDialogFragment.TAG)
    }

    private fun onPeriodDeleted() {
        viewModel.refresh()
        setFragmentResult(Keys.KEY_PERIODIC_CATEGORIES_SCREEN_ON_PERIODS_LIST_CHANGED, Bundle.EMPTY)
    }

}
