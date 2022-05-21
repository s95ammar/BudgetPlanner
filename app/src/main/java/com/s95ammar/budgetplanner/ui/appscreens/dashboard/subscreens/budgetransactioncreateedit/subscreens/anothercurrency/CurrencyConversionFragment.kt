package com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgetransactioncreateedit.subscreens.anothercurrency

import android.view.View
import androidx.core.os.bundleOf
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import com.s95ammar.budgetplanner.MobileNavigationDirections
import com.s95ammar.budgetplanner.R
import com.s95ammar.budgetplanner.databinding.FragmentCurrencyConversionBinding
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgetransactioncreateedit.subscreens.anothercurrency.data.ConversionResult
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgetransactioncreateedit.subscreens.anothercurrency.data.CurrencyConversionUiEvent
import com.s95ammar.budgetplanner.ui.common.Keys
import com.s95ammar.budgetplanner.ui.common.data.IntCurrencySelectionType
import com.s95ammar.budgetplanner.ui.common.viewbinding.BaseViewBinderFragment
import com.s95ammar.budgetplanner.ui.main.data.Currency
import com.s95ammar.budgetplanner.util.getAmountStringFormatted
import com.s95ammar.budgetplanner.util.lifecycleutil.observeEvent
import com.s95ammar.budgetplanner.util.orZero
import com.s95ammar.budgetplanner.util.text
import com.s95ammar.budgetplanner.util.updateTextIfNotEquals
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CurrencyConversionFragment : BaseViewBinderFragment<FragmentCurrencyConversionBinding>(R.layout.fragment_currency_conversion) {

    private val viewModel: CurrencyConversionViewModel by viewModels()

    override fun initViewBinding(view: View): FragmentCurrencyConversionBinding {
        return FragmentCurrencyConversionBinding.bind(view)
    }

    override fun setUpViews() {
        super.setUpViews()
        binding.toolbar.setNavigationOnClickListener { viewModel.onBack() }
        binding.fromCurrencyInputLayout.editText?.doAfterTextChanged { viewModel.onFromAmountChanged(it.toString().toDoubleOrNull().orZero()) }
        binding.conversionRateInputLayout.editText?.doAfterTextChanged { viewModel.onConversionRateChanged(it.toString().toDoubleOrNull().orZero()) }
        binding.fromCurrencyTextView.setOnClickListener { viewModel.onChangeFromCurrency() }
        binding.buttonApply.setOnClickListener { viewModel.onApply() }
    }

    override fun initObservers() {
        super.initObservers()
        viewModel.fromCurrencyCode.observe(viewLifecycleOwner) { setFromCurrencyCode(it) }
        viewModel.toCurrencyCode.observe(viewLifecycleOwner) { setToCurrencyCode(it) }
        viewModel.fromAmount.observe(viewLifecycleOwner) { setFromAmount(it) }
        viewModel.conversionRate.observe(viewLifecycleOwner) { setConversionRate(it) }
        viewModel.conversionResult.observe(viewLifecycleOwner) { setConversionResult(it) }
        viewModel.performUiEvent.observeEvent(viewLifecycleOwner) { performUiEvent(it) }
    }

    private fun performUiEvent(uiEvent: CurrencyConversionUiEvent) {
        when (uiEvent) {
            is CurrencyConversionUiEvent.DisplayLoadingState -> activityViewModel.setMainLoadingState(uiEvent.loadingState)
            is CurrencyConversionUiEvent.NavigateToCurrencySelection -> listenAndNavigateToCurrencySelection(uiEvent.currentCurrencyCode)
            is CurrencyConversionUiEvent.SetResult -> setResult(uiEvent.amount)
            is CurrencyConversionUiEvent.Exit -> navController.navigateUp()
        }
    }

    private fun setFromCurrencyCode(fromCode: String) {
        binding.fromCurrencyTextView.text = fromCode
        binding.conversionRateDescFromTextView.text = getString(R.string.format_conversion_rate_desc_from, fromCode)
    }

    private fun setToCurrencyCode(toCode: String) {
        binding.conversionRateDescToTextView.text = toCode
    }

    private fun setFromAmount(from: Double) {
        if (from != binding.fromCurrencyInputLayout.text?.toDoubleOrNull()) {
            binding.fromCurrencyInputLayout.updateTextIfNotEquals(
                from.takeIf { it != 0.0 }?.let { amount ->
                    getAmountStringFormatted(amount, isForEditText = true)
                }
            )
        }
    }

    private fun setConversionRate(rate: Double) {
        if (rate != binding.conversionRateInputLayout.text?.toDoubleOrNull()) {
            binding.conversionRateInputLayout.updateTextIfNotEquals(
                rate.takeIf { it != 0.0 }?.let { amount ->
                    getAmountStringFormatted(amount, isForEditText = true)
                }
            )
        }
    }

    private fun setConversionResult(conversionResult: ConversionResult) {
        val amountFormatted = getAmountStringFormatted(conversionResult.amount, includePlusSign = false)
        binding.conversionResultTextView.text = getString(
            R.string.format_conversion_result,
            conversionResult.currencyCode,
            amountFormatted
        )
    }

    private fun listenAndNavigateToCurrencySelection(currencyCode: String?) {
        setFragmentResultListener(Keys.KEY_CURRENCY_REQUEST) { _, bundle ->
            bundle.getParcelable<Currency>(Keys.KEY_CURRENCY)?.let { currency ->
                viewModel.onFromCurrencyChanged(currency.code)
            }
        }
        navController.navigate(
            MobileNavigationDirections
                .actionGlobalCurrencySelectionFragment(currencyCode, IntCurrencySelectionType.BUDGET_TRANSACTION_CURRENCY)
        )
    }

    private fun setResult(amount: Double) {
        setFragmentResult(Keys.KEY_AMOUNT_REQUEST, bundleOf(Keys.KEY_AMOUNT to amount))
    }

}