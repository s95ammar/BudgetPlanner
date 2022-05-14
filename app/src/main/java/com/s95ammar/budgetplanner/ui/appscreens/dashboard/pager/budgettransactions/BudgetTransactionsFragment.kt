package com.s95ammar.budgetplanner.ui.appscreens.dashboard.pager.budgettransactions

import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import com.s95ammar.budgetplanner.R
import com.s95ammar.budgetplanner.databinding.FragmentDashboardTransactionsBinding
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.DashboardSharedViewModel
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.common.data.BudgetTransaction
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.pager.budgettransactions.adapter.BudgetTransactionsAdapter
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.pager.budgettransactions.adapter.BudgetTransactionsItemType
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.pager.budgettransactions.data.BudgetTransactionsUiEvent
import com.s95ammar.budgetplanner.ui.common.LoadingState
import com.s95ammar.budgetplanner.ui.common.bottomsheet.EditDeleteBottomSheetDialogFragment
import com.s95ammar.budgetplanner.ui.common.viewbinding.BaseViewBinderFragment
import com.s95ammar.budgetplanner.util.lifecycleutil.observeEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BudgetTransactionsFragment : BaseViewBinderFragment<FragmentDashboardTransactionsBinding>(R.layout.fragment_dashboard_transactions) {

    companion object {
        fun newInstance() = BudgetTransactionsFragment()
    }

    private val viewModel: BudgetTransactionsViewModel by viewModels()
    private val sharedViewModel: DashboardSharedViewModel by hiltNavGraphViewModels(R.id.nested_navigation_dashboard)

    private val adapter by lazy {
        BudgetTransactionsAdapter(
            viewModel::onShowOnMap,
            viewModel::onBudgetTransactionClick,
            viewModel::onBudgetTransactionLongClick
        )
    }

    override fun initViewBinding(view: View): FragmentDashboardTransactionsBinding {
        return FragmentDashboardTransactionsBinding.bind(view)
    }

    override fun setUpViews() {
        super.setUpViews()
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        binding.recyclerView.adapter = adapter
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
    }

    override fun initObservers() {
        super.initObservers()
        viewModel.budgetTransactionItems.observe(viewLifecycleOwner) { setBudgetTransactions(it) }
        sharedViewModel.selectedPeriodId.observe(viewLifecycleOwner) { viewModel.onPeriodChanged(it) }

        viewModel.performUiEvent.observeEvent(viewLifecycleOwner) { performUiEvent(it) }
    }

    private fun setBudgetTransactions(items: List<BudgetTransactionsItemType>) {
        adapter.submitList(items) {
            if (items.isNotEmpty()) binding.recyclerView.layoutManager?.scrollToPosition(0)
        }
        binding.recyclerView.isGone = items.isEmpty()
        binding.instructionsLayout.root.isVisible = items.isEmpty()
        binding.instructionsLayout.messageTextView.text = getString(R.string.instruction_budget_transactions_screen_no_data)

    }

    private fun performUiEvent(uiEvent: BudgetTransactionsUiEvent) {
        when (uiEvent) {
            is BudgetTransactionsUiEvent.DisplayLoadingState -> handleLoadingState(uiEvent.loadingState)
            is BudgetTransactionsUiEvent.NavigateToEditBudgetTransaction -> sharedViewModel.onNavigateToEditBudgetTransaction(
                uiEvent.periodId,
                uiEvent.budgetTransactionId
            )
            is BudgetTransactionsUiEvent.ShowBottomSheet -> showBottomSheet(uiEvent.budgetTransaction)
            is BudgetTransactionsUiEvent.ShowOnMap -> showOnMap()
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

    private fun showBottomSheet(budgetTransaction: BudgetTransaction) {
        EditDeleteBottomSheetDialogFragment.newInstance(budgetTransaction.name, R.drawable.ic_budget_transaction).apply {
            listener = object : EditDeleteBottomSheetDialogFragment.Listener {
                override fun onEdit() {
                    sharedViewModel.onNavigateToEditBudgetTransaction(budgetTransaction.periodId, budgetTransaction.id)
                }

                override fun onDelete() {
                    displayDeleteConfirmationDialog(budgetTransaction.name) { viewModel.deleteBudgetTransaction(budgetTransaction.id) }
                }
            }
        }.show(childFragmentManager, EditDeleteBottomSheetDialogFragment.TAG)
    }

    private fun showOnMap() {
        // TODO
        showSnackbar("to be implemented")
    }

    override fun showLoading() {
        binding.progressBar.isVisible = true
    }

    override fun hideLoading() {
        binding.progressBar.isGone = true
    }
}