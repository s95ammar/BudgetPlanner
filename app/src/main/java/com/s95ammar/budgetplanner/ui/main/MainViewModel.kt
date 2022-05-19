package com.s95ammar.budgetplanner.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.s95ammar.budgetplanner.models.repository.CurrencyRepository
import com.s95ammar.budgetplanner.ui.common.LoadingState
import com.s95ammar.budgetplanner.ui.main.data.Currency
import com.s95ammar.budgetplanner.ui.main.data.MainUiEvent
import com.s95ammar.budgetplanner.util.lifecycleutil.EventMutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val currencyRepository: CurrencyRepository
) : ViewModel() {

    private val _mainCurrency = MutableLiveData<Currency>()
    private val _performUiEvent = EventMutableLiveData<MainUiEvent>()

    val performUiEvent = _performUiEvent.asEventLiveData()

    init {
        loadMainCurrency()
    }

    fun finishActivity() {
        _performUiEvent.call(MainUiEvent.FinishActivity)
    }

    fun saveNewMainCurrency(
        currency: Currency
    ) {
        viewModelScope.launch {
            val cachingRequestFlow = Currency.EntityMapper.toEntity(currency)?.let { currencyEntity ->
                currencyRepository.insertCurrency(currencyEntity)
            } ?: emptyFlow()

            cachingRequestFlow.flatMapConcat {
                currencyRepository.setMainCurrencyCode(currency.code)
            }.onStart {
                _performUiEvent.call(MainUiEvent.DisplayLoadingState(LoadingState.Loading))
            }.catch { throwable ->
                _performUiEvent.call(MainUiEvent.DisplayLoadingState(LoadingState.Error(throwable)))
            }.collect {
                _performUiEvent.call(MainUiEvent.DisplayLoadingState(LoadingState.Success))
            }
        }
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
}