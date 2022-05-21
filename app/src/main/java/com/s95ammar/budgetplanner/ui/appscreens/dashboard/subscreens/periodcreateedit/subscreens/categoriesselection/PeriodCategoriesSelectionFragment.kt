package com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.periodcreateedit.subscreens.categoriesselection

import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.s95ammar.budgetplanner.MobileNavigationDirections
import com.s95ammar.budgetplanner.R
import com.s95ammar.budgetplanner.databinding.FragmentPeriodCategoriesSelectionBinding
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.common.data.PeriodicCategory
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.periodcreateedit.PeriodCreateEditSharedViewModel
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.periodcreateedit.subscreens.categoriesselection.adapter.PeriodicCategoriesMultiSelectionAdapter
import com.s95ammar.budgetplanner.ui.common.Keys
import com.s95ammar.budgetplanner.ui.common.data.IntCurrencySelectionType
import com.s95ammar.budgetplanner.ui.common.viewbinding.BaseViewBinderFragment
import com.s95ammar.budgetplanner.ui.main.data.Currency
import com.s95ammar.budgetplanner.util.lifecycleutil.observeEvent
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.periodcreateedit.subscreens.categoriesselection.data.PeriodCategoriesSelectionUiEvent as UiEvent

class PeriodCategoriesSelectionFragment :
    BaseViewBinderFragment<FragmentPeriodCategoriesSelectionBinding>(R.layout.fragment_period_categories_selection) {

    private val viewModel: PeriodCategoriesSelectionViewModel by viewModels()
    private val sharedViewModel: PeriodCreateEditSharedViewModel by hiltNavGraphViewModels(R.id.nested_period_create_edit)

    private val adapter by lazy {
        PeriodicCategoriesMultiSelectionAdapter(
            onSelectionStateChanged = sharedViewModel::onPeriodicCategorySelectionStateChanged,
            onCreateEditEstimate = viewModel::onCreateEditEstimate,
            onChangeCurrency = viewModel::onChangeCurrency
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
            is UiEvent.NavigateToCurrencySelection -> listenAndNavigateToCurrencySelection(uiEvent.periodicCategory)
            is UiEvent.NavigateToCreateEditEstimate -> listenAndNavigateToEstimateCreateEdit(uiEvent.periodicCategory)
            is UiEvent.Exit -> navController.navigateUp()
        }
    }

    private fun listenAndNavigateToCurrencySelection(periodicCategory: PeriodicCategory) {
        setFragmentResultListener(Keys.KEY_CURRENCY_REQUEST) { _, bundle ->
            bundle.getParcelable<Currency>(Keys.KEY_CURRENCY)?.let { currency ->
                sharedViewModel.onPeriodicCategoryCurrencyChanged(periodicCategory, currency)
            }
        }
        navController.navigate(
            MobileNavigationDirections
                .actionGlobalCurrencySelectionFragment(
                    currentCurrencyCode = periodicCategory.currencyCode,
                    currencySelectionType = IntCurrencySelectionType.PERIODIC_CATEGORY_CURRENCY
                )
        )
    }

    private fun listenAndNavigateToEstimateCreateEdit(periodicCategory: PeriodicCategory) {
        setFragmentResultListener(Keys.KEY_ESTIMATE_REQUEST) { _, bundle ->
            val estimateOrNull = bundle.getDouble(Keys.KEY_ESTIMATE, 0.0)
            sharedViewModel.onPeriodicCategoryEstimateChanged(periodicCategory, estimateOrNull)
        }
        navController.navigate(
            PeriodCategoriesSelectionFragmentDirections
                .actionPeriodCategoriesSelectionFragmentToPeriodCategoryEstimateCreateEditFragment(periodicCategory)
        )
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