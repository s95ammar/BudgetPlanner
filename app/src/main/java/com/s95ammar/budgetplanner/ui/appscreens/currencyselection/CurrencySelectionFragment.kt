package com.s95ammar.budgetplanner.ui.appscreens.currencyselection

import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import com.s95ammar.budgetplanner.R
import com.s95ammar.budgetplanner.databinding.FragmentCurrencySelectionBinding
import com.s95ammar.budgetplanner.ui.appscreens.currencyselection.adapter.CurrencySelectionAdapter
import com.s95ammar.budgetplanner.ui.appscreens.currencyselection.adapter.CurrencySelectionItemType
import com.s95ammar.budgetplanner.ui.appscreens.currencyselection.data.CurrencySelectionUiEvent
import com.s95ammar.budgetplanner.ui.common.Keys
import com.s95ammar.budgetplanner.ui.common.LoadingState
import com.s95ammar.budgetplanner.ui.common.data.IntCurrencySelectionType
import com.s95ammar.budgetplanner.ui.common.viewbinding.BaseViewBinderFragment
import com.s95ammar.budgetplanner.ui.main.data.Currency
import com.s95ammar.budgetplanner.util.lifecycleutil.observeEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CurrencySelectionFragment : BaseViewBinderFragment<FragmentCurrencySelectionBinding>(
    R.layout.fragment_currency_selection
) {

    private val viewModel: CurrencySelectionViewModel by viewModels()
    private val adapter: CurrencySelectionAdapter by lazy {
        CurrencySelectionAdapter(viewModel::onCurrencyClick, viewModel::onLoadMoreCurrencies)
    }

    override fun initViewBinding(view: View): FragmentCurrencySelectionBinding {
        return FragmentCurrencySelectionBinding.bind(view)
    }

    override fun setUpViews() {
        super.setUpViews()
        setUpRecyclerView()
    }

    override fun initObservers() {
        super.initObservers()
        viewModel.currenciesItems.observe(viewLifecycleOwner) { setCurrenciesList(it) }
        viewModel.performUiEvent.observeEvent(viewLifecycleOwner) { performUiEvent(it) }
        viewModel.currencySelectionType.observe(viewLifecycleOwner) { setViewsToCurrencySelectionType(it) }
    }

    private fun setUpRecyclerView() {
        binding.recyclerView.adapter = adapter
        binding.recyclerView.setHasFixedSize(true)
    }

    private fun setCurrenciesList(items: List<CurrencySelectionItemType>) {
        adapter.submitList(items) {
            executeIfViewIsAvailable {
                if (items.isNotEmpty()) binding.recyclerView.layoutManager?.scrollToPosition(0)
            }
        }
    }

    private fun performUiEvent(uiEvent: CurrencySelectionUiEvent) {
        when (uiEvent) {
            is CurrencySelectionUiEvent.DisplayLoadingState -> handleLoadingState(uiEvent.loadingState)
            is CurrencySelectionUiEvent.SetResult -> setResult(uiEvent.currency, uiEvent.isMainCurrencySelection)
            is CurrencySelectionUiEvent.Exit -> navController.navigateUp()
            is CurrencySelectionUiEvent.FinishActivity -> activityViewModel.finishActivity()
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

    private fun setResult(currency: Currency, isMainCurrencySelection: Boolean) {
        if (isMainCurrencySelection) {
            activityViewModel.onMainCurrencyChanged(currency)
        } else {
            setFragmentResult(Keys.KEY_CURRENCY_REQUEST, bundleOf(Keys.KEY_CURRENCY to currency))
        }
    }

    private fun setViewsToCurrencySelectionType(@IntCurrencySelectionType currencySelectionType: Int) {
        setUpTipPreview(currencySelectionType)
        setUpToolbarAndBackFunctionality(
            isBackAvailable = currencySelectionType != IntCurrencySelectionType.MAIN_CURRENCY
        )
    }

    private fun setUpTipPreview(@IntCurrencySelectionType currencySelectionType: Int) {
        when (currencySelectionType) {
            IntCurrencySelectionType.MAIN_CURRENCY -> {
                binding.tipTextView.setText(R.string.select_main_currency_tip)
            }
            IntCurrencySelectionType.PERIODIC_CATEGORY_CURRENCY -> {
                binding.tipTextView.setText(R.string.select_periodic_category_currency_tip)
            }
            IntCurrencySelectionType.BUDGET_TRANSACTION_CURRENCY -> binding.tipTextView.isVisible = false
        }
    }

    private fun setUpToolbarAndBackFunctionality(isBackAvailable: Boolean) {
        if (isBackAvailable) {
            binding.toolbar.navigationIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_arrow_back)
            binding.toolbar.setNavigationOnClickListener { viewModel.onBack() }
        } else {
            binding.toolbar.navigationIcon = null
            requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    viewModel.onBack()
                }
            })
        }
    }
}