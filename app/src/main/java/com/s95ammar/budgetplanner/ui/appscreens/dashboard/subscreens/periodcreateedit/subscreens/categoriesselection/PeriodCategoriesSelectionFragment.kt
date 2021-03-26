package com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.periodcreateedit.subscreens.categoriesselection

import android.view.View
import androidx.fragment.app.viewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.s95ammar.budgetplanner.R
import com.s95ammar.budgetplanner.databinding.FragmentPeriodCategoriesSelectionBinding
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.common.data.PeriodicCategoryViewEntity
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.periodcreateedit.PeriodCreateEditSharedViewModel
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.periodcreateedit.subscreens.categoriesselection.adapter.PeriodicCategoriesSelectionAdapter
import com.s95ammar.budgetplanner.ui.base.BaseFragment
import com.s95ammar.budgetplanner.ui.common.viewbinding.ViewBinder
import com.s95ammar.budgetplanner.util.lifecycleutil.observeEvent
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.periodcreateedit.subscreens.categoriesselection.data.PeriodCategoriesSelectionUiEvent as UiEvent

class PeriodCategoriesSelectionFragment : BaseFragment(R.layout.fragment_period_categories_selection),
    ViewBinder<FragmentPeriodCategoriesSelectionBinding> {

    private val viewModel: PeriodCategoriesSelectionViewModel by viewModels()
    private val sharedViewModel: PeriodCreateEditSharedViewModel by hiltNavGraphViewModels(R.id.nested_period_create_edit)

    private val adapter by lazy {
        PeriodicCategoriesSelectionAdapter(
            onClick = sharedViewModel::onPeriodicCategorySelectionStateChanged,
            onMaxInputChanged = sharedViewModel::onPeriodicCategoryMaxChanged
        )
    }

    override val binding: FragmentPeriodCategoriesSelectionBinding
        get() = getBinding()

    override fun initViewBinding(view: View): FragmentPeriodCategoriesSelectionBinding {
        return FragmentPeriodCategoriesSelectionBinding.bind(view)
    }

    override fun setUpViews() {
        super.setUpViews()
        binding.toolbar.setNavigationOnClickListener { viewModel.onBack() }
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
    }

    override fun initObservers() {
        super.initObservers()
        sharedViewModel.alwaysAllowCategorySelection.observe(viewLifecycleOwner) { setAlwaysAllowCategorySelection(it) }

        viewModel.performUiEvent.observeEvent(viewLifecycleOwner) { performUiEvent(it) }
        sharedViewModel.periodicCategories.observe(viewLifecycleOwner) { setPeriodicCategories(it) }
    }

    private fun performUiEvent(uiEvent: UiEvent) {
        when (uiEvent) {
            UiEvent.Exit -> navController.navigateUp()
        }
    }

    private fun setAlwaysAllowCategorySelection(alwaysAllowCategorySelection: Boolean) {
        adapter.alwaysAllowCategorySelection = alwaysAllowCategorySelection
    }

    private fun setPeriodicCategories(periodicCategories: List<PeriodicCategoryViewEntity>) {
        adapter.submitList(periodicCategories)
    }

}