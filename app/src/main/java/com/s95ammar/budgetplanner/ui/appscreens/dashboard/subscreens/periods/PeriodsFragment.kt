package com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.periods

import android.os.Bundle
import android.view.View
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import com.s95ammar.budgetplanner.R
import com.s95ammar.budgetplanner.databinding.FragmentPeriodsBinding
import com.s95ammar.budgetplanner.models.view.PeriodSimpleViewEntity
import com.s95ammar.budgetplanner.ui.appscreens.auth.common.LoadingState
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.periods.adapter.PeriodsListAdapter
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.periods.data.PeriodsUiEvent
import com.s95ammar.budgetplanner.ui.base.BaseFragment
import com.s95ammar.budgetplanner.ui.common.Keys
import com.s95ammar.budgetplanner.ui.common.bottomsheet.EditDeleteBottomSheetDialogFragment
import com.s95ammar.budgetplanner.ui.common.viewbinding.ViewBinder
import com.s95ammar.budgetplanner.util.lifecycleutil.observeEvent
import dagger.hilt.android.AndroidEntryPoint

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
        viewModel.onPeriodDeleted.observeEvent(viewLifecycleOwner) { onPeriodDeleted() }
    }

    override fun showLoading() {
        binding.swipeToRefreshLayout.isRefreshing = true
    }

    override fun hideLoading() {
        binding.swipeToRefreshLayout.isRefreshing = false
    }

    private fun setAllPeriods(periods: List<PeriodSimpleViewEntity>) {
        adapter.submitList(periods)
    }

    private fun performUiEvent(uiEvent: PeriodsUiEvent) {
        when (uiEvent) {
            is PeriodsUiEvent.DisplayLoadingState -> handleLoadingState(uiEvent.loadingState)
            is PeriodsUiEvent.OnNavigateToEditPeriod -> onNavigateToCreateEditPeriod(uiEvent.periodId)
            is PeriodsUiEvent.ShowBottomSheet -> showBottomSheet(uiEvent.period)
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

    private fun onNavigateToCreateEditPeriod(periodId: Int) {
        setFragmentResultListener(Keys.KEY_ON_PERIOD_CREATE_EDIT) { _, _ -> viewModel.refresh() }
        navController.navigate(
            PeriodsFragmentDirections.actionPeriodsFragmentToPeriodCreateEditFragment(periodId)
        )
    }

    private fun showBottomSheet(period: PeriodSimpleViewEntity) {
        EditDeleteBottomSheetDialogFragment.newInstance(period.name, R.drawable.ic_period).apply {
            listener = object : EditDeleteBottomSheetDialogFragment.Listener {
                override fun onEdit() = onNavigateToCreateEditPeriod(period.id)
                override fun onDelete() = displayDeleteConfirmationDialog(period.name) { viewModel.deletePeriod(period.id) }
            }
        }.show(childFragmentManager, EditDeleteBottomSheetDialogFragment.TAG)
    }

    private fun onPeriodDeleted() {
        viewModel.refresh()
        setFragmentResult(Keys.KEY_PERIOD_RECORDS_SCREEN_ON_PERIODS_LIST_CHANGED, Bundle.EMPTY)
    }

}
