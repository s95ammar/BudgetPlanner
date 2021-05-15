package com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.periods

import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import com.s95ammar.budgetplanner.R
import com.s95ammar.budgetplanner.databinding.FragmentPeriodsBinding
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.common.data.PeriodSimple
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.periods.adapter.PeriodsAdapter
import com.s95ammar.budgetplanner.ui.common.LoadingState
import com.s95ammar.budgetplanner.ui.common.bottomsheet.EditDeleteBottomSheetDialogFragment
import com.s95ammar.budgetplanner.ui.common.viewbinding.BaseViewBinderFragment
import com.s95ammar.budgetplanner.util.lifecycleutil.observeEvent
import dagger.hilt.android.AndroidEntryPoint
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.periods.data.PeriodsUiEvent as UiEvent

@AndroidEntryPoint
class PeriodsFragment : BaseViewBinderFragment<FragmentPeriodsBinding>(R.layout.fragment_periods) {

    private val viewModel: PeriodsViewModel by viewModels()
    private val adapter by lazy { PeriodsAdapter(viewModel::onPeriodItemClick, viewModel::onPeriodItemLongClick) }

    override fun initViewBinding(view: View): FragmentPeriodsBinding {
        return FragmentPeriodsBinding.bind(view)
    }

    override fun setUpViews() {
        super.setUpViews()
        binding.toolbar.setNavigationOnClickListener { navController.navigateUp() }
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

    private fun setAllPeriods(periods: List<PeriodSimple>) {
        adapter.submitList(periods)
    }

    private fun performUiEvent(uiEvent: UiEvent) {
        when (uiEvent) {
            is UiEvent.DisplayLoadingState -> handleLoadingState(uiEvent.loadingState)
            is UiEvent.ListenAndNavigateToEditPeriod -> navigateToCreateEditPeriod(uiEvent.period)
            is UiEvent.ShowBottomSheet -> showBottomSheet(uiEvent.period)
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
                showErrorSnackbar(loadingState.throwable)
            }
        }
    }

    private fun navigateToCreateEditPeriod(period: PeriodSimple) {
        navController.navigate(
            PeriodsFragmentDirections.actionPeriodsFragmentToNestedPeriodCreateEdit(period)
        )
    }

    private fun showBottomSheet(period: PeriodSimple) {
        EditDeleteBottomSheetDialogFragment.newInstance(period.name, R.drawable.ic_period).apply {
            listener = object : EditDeleteBottomSheetDialogFragment.Listener {
                override fun onEdit() = navigateToCreateEditPeriod(period)
                override fun onDelete() = displayDeleteConfirmationDialog(period.name) { viewModel.deletePeriod(period.id) }
            }
        }.show(childFragmentManager, EditDeleteBottomSheetDialogFragment.TAG)
    }

}
