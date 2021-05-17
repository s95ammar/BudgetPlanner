package com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.periodcreateedit.subscreens.categoriesselection

import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.s95ammar.budgetplanner.R
import com.s95ammar.budgetplanner.databinding.FragmentPeriodCategoriesSelectionBinding
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.common.data.PeriodicCategory
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.periodcreateedit.PeriodCreateEditSharedViewModel
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.periodcreateedit.subscreens.categoriesselection.adapter.PeriodicCategoriesMultiSelectionAdapter
import com.s95ammar.budgetplanner.ui.common.viewbinding.BaseViewBinderFragment
import com.s95ammar.budgetplanner.util.lifecycleutil.observeEvent
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.periodcreateedit.subscreens.categoriesselection.data.PeriodCategoriesSelectionUiEvent as UiEvent

class PeriodCategoriesSelectionFragment :
    BaseViewBinderFragment<FragmentPeriodCategoriesSelectionBinding>(R.layout.fragment_period_categories_selection) {

    private val viewModel: PeriodCategoriesSelectionViewModel by viewModels()
    private val sharedViewModel: PeriodCreateEditSharedViewModel by hiltNavGraphViewModels(R.id.nested_period_create_edit)

    private val adapter by lazy {
        PeriodicCategoriesMultiSelectionAdapter(
            onClick = sharedViewModel::onPeriodicCategorySelectionStateChanged,
            onMaxInputChanged = sharedViewModel::onPeriodicCategoryMaxChanged
        )
    }

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
        sharedViewModel.allowCategorySelectionForAll.observe(viewLifecycleOwner) { setAllowCategorySelectionForAll(it) }

        viewModel.performUiEvent.observeEvent(viewLifecycleOwner) { performUiEvent(it) }
        sharedViewModel.periodicCategories.observe(viewLifecycleOwner) { setPeriodicCategories(it) }
    }

    private fun performUiEvent(uiEvent: UiEvent) {
        when (uiEvent) {
            UiEvent.Exit -> navController.navigateUp()
        }
    }

    private fun setAllowCategorySelectionForAll(allowCategorySelectionForAll: Boolean) {
        adapter.allowCategorySelectionForAll = allowCategorySelectionForAll
    }

    private fun setPeriodicCategories(periodicCategories: List<PeriodicCategory>) {
        adapter.submitList(periodicCategories)
        binding.recyclerView.isGone = periodicCategories.isEmpty()
        binding.instructionsLayout.root.isVisible = periodicCategories.isEmpty()
        binding.instructionsLayout.messageTextView.text = getString(R.string.instruction_period_create_edit_screen_create_category)
    }

}