package com.s95ammar.budgetplanner.ui.appscreens.currencyselection

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.viewModelScope
import com.s95ammar.budgetplanner.models.repository.CurrencyRepository
import com.s95ammar.budgetplanner.ui.appscreens.currencyselection.adapter.CurrencySelectionItemType
import com.s95ammar.budgetplanner.ui.appscreens.currencyselection.data.CurrencySelectionUiEvent
import com.s95ammar.budgetplanner.ui.common.LoadingState
import com.s95ammar.budgetplanner.ui.common.data.IntCurrencySelectionType
import com.s95ammar.budgetplanner.ui.common.data.Selectable
import com.s95ammar.budgetplanner.ui.main.data.Currency
import com.s95ammar.budgetplanner.util.lifecycleutil.EventMutableLiveData
import com.s95ammar.budgetplanner.util.lifecycleutil.LoaderMutableLiveData
import com.s95ammar.budgetplanner.util.lifecycleutil.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrencySelectionViewModel @Inject constructor(
    private val repository: CurrencyRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val currentCurrencyCode = savedStateHandle.get<String?>(
        CurrencySelectionFragmentArgs::currentCurrencyCode.name
    )
    private val _currencySelectionType = savedStateHandle.getLiveData<@IntCurrencySelectionType Int>(
        CurrencySelectionFragmentArgs::currencySelectionType.name
    )
    private val _currencies = LoaderMutableLiveData<List<Currency>> {
        loadCachedCurrencies()
    }
    private val _loadMoreLoadingState = MutableLiveData<LoadingState>(LoadingState.Cold)
    private val _performUiEvent = EventMutableLiveData<CurrencySelectionUiEvent>()

    private val isMainCurrencySelection = _currencySelectionType.value == IntCurrencySelectionType.MAIN_CURRENCY

    val currencySelectionType = _currencySelectionType.asLiveData()
    val currenciesItems = MediatorLiveData<List<CurrencySelectionItemType>>().apply {
        fun update() {
            value = createCurrencyItems(_currencies.value, _loadMoreLoadingState.value)
        }
        addSource(_currencies) { update() }
        addSource(_loadMoreLoadingState) { update() }
    }.distinctUntilChanged()
    val performUiEvent = _performUiEvent.asEventLiveData()

    fun onCurrencyClick(currency: Currency) {
        viewModelScope.launch {
            _performUiEvent.call(
                CurrencySelectionUiEvent.SetResult(
                    currency = currency,
                    isMainCurrencySelection = isMainCurrencySelection
                )
            )
            _performUiEvent.call(CurrencySelectionUiEvent.Exit)
        }
    }

    fun onLoadMoreCurrencies() {
        viewModelScope.launch {
            repository.loadCurrenciesList()
                .onStart {
                    _loadMoreLoadingState.value = LoadingState.Loading
                }
                .catch { throwable ->
                    _loadMoreLoadingState.value = LoadingState.Error(throwable)
                }
                .collect { currencyDtoList ->
                    _loadMoreLoadingState.value = LoadingState.Success
                    _currencies.value = buildSet {
                        addAll(currencyDtoList.mapNotNull(Currency.EntityMapper::fromEntity))
                        addAll(_currencies.value.orEmpty())
                    }.toList()
                }
        }
    }

    fun onBack() {
        if (isMainCurrencySelection) {
            _performUiEvent.call(CurrencySelectionUiEvent.FinishActivity)
        } else {
            _performUiEvent.call(CurrencySelectionUiEvent.Exit)
        }
    }

    private fun loadCachedCurrencies() {
        viewModelScope.launch {
            repository.getDbCurrenciesFlow()
                .onStart {
                    _performUiEvent.call(CurrencySelectionUiEvent.DisplayLoadingState(LoadingState.Loading))
                }
                .catch { throwable ->
                    _performUiEvent.call(CurrencySelectionUiEvent.DisplayLoadingState(LoadingState.Error(throwable)))
                }
                .collect { currencyEntityList ->
                    _performUiEvent.call(CurrencySelectionUiEvent.DisplayLoadingState(LoadingState.Success))
                    _currencies.value = currencyEntityList.mapNotNull(Currency.EntityMapper::fromEntity)
                }
        }
    }

    private fun createCurrencyItems(
        currencies: List<Currency>?,
        loadingState: LoadingState?
    ): List<CurrencySelectionItemType> {
        if (currencies == null || loadingState == null) return emptyList()

        val currencyItems = currencies.map { currency ->
            CurrencySelectionItemType.ListItem(Selectable(currency, isSelected = currency.code == currentCurrencyCode))
        }

        return buildList {
            addAll(currencyItems.sortedBy { it.selectableCurrency.value.code })
            add(CurrencySelectionItemType.LoadMore(loadingState))
        }

    }
}