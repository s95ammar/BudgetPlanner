package com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgetransactioncreateedit.subscreens.anothercurrency

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.s95ammar.budgetplanner.models.repository.CurrencyRepository
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgetransactioncreateedit.subscreens.anothercurrency.data.ConversionResult
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgetransactioncreateedit.subscreens.anothercurrency.data.CurrencyConversionUiEvent
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgetransactioncreateedit.subscreens.anothercurrency.data.CurrencyRatesMap
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgetransactioncreateedit.subscreens.anothercurrency.data.CurrencyRatesMapDtoMapper
import com.s95ammar.budgetplanner.ui.common.LoadingState
import com.s95ammar.budgetplanner.util.lifecycleutil.EventMutableLiveData
import com.s95ammar.budgetplanner.util.lifecycleutil.LoaderMutableLiveData
import com.s95ammar.budgetplanner.util.orZero
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrencyConversionViewModel @Inject constructor(
    private val repository: CurrencyRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val toCurrencyCodeStr = savedStateHandle.get<String>(
        CurrencyConversionFragmentArgs::toCurrencyCode.name
    ).orEmpty()
    private val _toCurrencyCode = MutableLiveData(toCurrencyCodeStr)
    private val _fromCurrencyCode = MutableLiveData(toCurrencyCodeStr)
    private val _toCurrencyRates = LoaderMutableLiveData<CurrencyRatesMap> { loadToCurrencyRates() }
    private val _conversionRate = MediatorLiveData<Double>().apply {
        fun update() {
            val rates = _toCurrencyRates.value.takeUnless { it.isNullOrEmpty() } ?: return
            val from = _fromCurrencyCode.value ?: return
            value = rates.getOrElse(from) { 1.0 }
        }
        addSource(_toCurrencyRates) { update() }
        addSource(_fromCurrencyCode) { update() }
    }
    private val _fromAmount = MutableLiveData<Double>()
    private val _toAmount = MediatorLiveData<Double>().apply {
        fun update() {
            value = _fromAmount.value.orZero() * _conversionRate.value.orZero()
        }
        addSource(_fromAmount) { update() }
        addSource(_conversionRate) { update() }
    }
    private val _performUiEvent = EventMutableLiveData<CurrencyConversionUiEvent>()

    val fromCurrencyCode = _fromCurrencyCode.distinctUntilChanged()
    val toCurrencyCode = _toCurrencyCode.distinctUntilChanged()
    val fromAmount = _fromAmount.distinctUntilChanged()
    val conversionRate = _conversionRate.distinctUntilChanged()
    val conversionResult = _toAmount.distinctUntilChanged().map { amount ->
        ConversionResult(amount, _toCurrencyCode.value.orEmpty())
    }
    val performUiEvent = _performUiEvent.asEventLiveData()

    fun onChangeFromCurrency() {
        _performUiEvent.call(
            CurrencyConversionUiEvent.NavigateToCurrencySelection(_fromCurrencyCode.value, isBackAllowed = true)
        )
    }

    fun onFromCurrencyChanged(fromCode: String) {
        _fromCurrencyCode.value = fromCode
    }

    fun onFromAmountChanged(from: Double) {
        _fromAmount.value = from
    }

    fun onConversionRateChanged(rate: Double) {
        _conversionRate.value = rate
    }

    fun onApply() {
        _performUiEvent.call(CurrencyConversionUiEvent.SetResult(_toAmount.value.orZero()))
        _performUiEvent.call(CurrencyConversionUiEvent.Exit)
    }

    fun onBack() {
        _performUiEvent.call(CurrencyConversionUiEvent.Exit)
    }

    private fun loadToCurrencyRates() {
        viewModelScope.launch {
            repository.loadConversionRates(toCurrencyCodeStr)
                .onStart {
                    _performUiEvent.call(CurrencyConversionUiEvent.DisplayLoadingState(LoadingState.Loading))
                }
                .catch { throwable ->
                    _performUiEvent.call(CurrencyConversionUiEvent.DisplayLoadingState(LoadingState.Error(throwable)))
                }
                .collect { conversionResponse ->
                    _performUiEvent.call(CurrencyConversionUiEvent.DisplayLoadingState(LoadingState.Success))
                    _toCurrencyRates.value = CurrencyRatesMapDtoMapper.fromDto(conversionResponse)
                }
        }
    }
}
