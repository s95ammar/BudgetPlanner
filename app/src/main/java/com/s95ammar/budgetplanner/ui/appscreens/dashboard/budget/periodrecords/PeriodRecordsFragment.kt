package com.s95ammar.budgetplanner.ui.appscreens.dashboard.budget.periodrecords

import android.os.Bundle
import android.view.View
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import com.s95ammar.budgetplanner.R
import com.s95ammar.budgetplanner.databinding.FragmentPeriodRecordsBinding
import com.s95ammar.budgetplanner.models.view.CategoryViewEntity
import com.s95ammar.budgetplanner.ui.appscreens.auth.common.LoadingState
import com.s95ammar.budgetplanner.ui.appscreens.categories.adapter.CategoriesListAdapter
import com.s95ammar.budgetplanner.ui.base.BaseFragment
import com.s95ammar.budgetplanner.ui.common.Keys
import com.s95ammar.budgetplanner.ui.common.viewbinding.ViewBinder
import com.s95ammar.budgetplanner.util.lifecycleutil.observeEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PeriodRecordsFragment : BaseFragment(R.layout.fragment_period_records), ViewBinder<FragmentPeriodRecordsBinding> {

    override val binding: FragmentPeriodRecordsBinding
        get() = getBinding()

    private val viewModel: PeriodRecordsViewModel by viewModels()
    private val adapter by lazy { CategoriesListAdapter(viewModel::onCategoryItemClick, onItemLongClick = null) }

    override fun initViewBinding(view: View): FragmentPeriodRecordsBinding {
        return FragmentPeriodRecordsBinding.bind(view)
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
        viewModel.resultCategories.observe(viewLifecycleOwner) { setAllCategories(it) }
        viewModel.displayLoadingState.observeEvent(viewLifecycleOwner) { handleLoadingState(it) }
        viewModel.onPeriodRecordAdded.observeEvent(viewLifecycleOwner) { onPeriodRecordAdded() }
    }

    private fun setAllCategories(categories: List<CategoryViewEntity>) {
        adapter.submitList(categories)
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

    private fun onPeriodRecordAdded() {
        setFragmentResult(Keys.KEY_ON_PERIOD_RECORD_ADDED, Bundle.EMPTY)
        navController.navigateUp()
    }

}
