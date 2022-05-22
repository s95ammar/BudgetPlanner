package com.s95ammar.budgetplanner.ui.main

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.s95ammar.budgetplanner.logFromHere
import com.s95ammar.budgetplanner.models.repository.CurrencyRepository
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgetransactioncreateedit.subscreens.anothercurrency.data.CurrencyRatesMap
import com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgetransactioncreateedit.subscreens.anothercurrency.data.CurrencyRatesMapDtoMapper
import com.s95ammar.budgetplanner.ui.common.LoadingState
import com.s95ammar.budgetplanner.ui.main.data.Currency
import com.s95ammar.budgetplanner.ui.main.data.CurrencyDetails
import com.s95ammar.budgetplanner.ui.main.data.MainUiEvent
import com.s95ammar.budgetplanner.util.asOptional
import com.s95ammar.budgetplanner.util.lifecycleutil.EventMutableLiveData
import com.s95ammar.budgetplanner.util.lifecycleutil.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val currencyRepository: CurrencyRepository
) : ViewModel() {

    private val _mainCurrency = MutableLiveData<Currency>()
    private val _mainCurrencyRates = MediatorLiveData<CurrencyRatesMap>().apply {
        addSource(_mainCurrency) { loadMainCurrencyRates(it.code) }
    }
    private val _performUiEvent = EventMutableLiveData<MainUiEvent>()

    val mainCurrency = _mainCurrency.asLiveData()
    val mainCurrencyRates = _mainCurrencyRates.asLiveData()
    val mainCurrencyDetails = MediatorLiveData<CurrencyDetails>().apply {
        fun update() {
            val mainCurrency = _mainCurrency.value ?: return
            val mainCurrencyRates = _mainCurrencyRates.value
            value = CurrencyDetails(mainCurrency.code, mainCurrencyRates.asOptional())
        }
        addSource(_mainCurrency) { update() }
        addSource(_mainCurrencyRates) { update() }
    }.asLiveData()
    val performUiEvent = _performUiEvent.asEventLiveData()

    init {
        loadMainCurrency()
    }

    fun finishActivity() {
        _performUiEvent.call(MainUiEvent.FinishActivity)
    }

    fun onMainCurrencyChanged(currency: Currency) {
        _mainCurrency.value = currency
    }

    fun setMainLoadingState(loadingState: LoadingState) {
        _performUiEvent.call(MainUiEvent.DisplayLoadingState(loadingState))
    }

    private fun loadMainCurrency() {
        viewModelScope.launch {
            currencyRepository.getMainCurrencyFlow()
                .catch { throwable ->
                    _performUiEvent.call(MainUiEvent.NavigateToMainCurrencySelection)
                }
                .collect { currencyEntity ->
                    if (currencyEntity == null) {
                        _performUiEvent.call(MainUiEvent.NavigateToMainCurrencySelection)
                    } else {
                        _mainCurrency.value = Currency.EntityMapper.fromEntity(currencyEntity)
                    }
                }
        }
    }

    private fun loadMainCurrencyRates(mainCurrencyCode: String) {
        viewModelScope.launch {
            currencyRepository.loadConversionRates(mainCurrencyCode)
                .catch { throwable ->
                    logFromHere(throwable)
                    _mainCurrencyRates.value = emptyMap()
                }
                .collect { conversionResponse ->
                    _mainCurrencyRates.value = CurrencyRatesMapDtoMapper.fromDto(conversionResponse)
                }
        }
    }
}