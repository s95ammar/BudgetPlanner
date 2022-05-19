package com.s95ammar.budgetplanner.ui.appscreens.currencyselection.data

import com.s95ammar.budgetplanner.ui.common.LoadingState
import com.s95ammar.budgetplanner.ui.main.data.Currency

sealed class CurrencySelectionUiEvent {
    data class DisplayLoadingState(val loadingState: LoadingState) : CurrencySelectionUiEvent()
    data class SetResult(val currency: Currency, val isMainCurrencySelection: Boolean) : CurrencySelectionUiEvent()
    object Exit : CurrencySelectionUiEvent()
    object FinishActivity : CurrencySelectionUiEvent()
}
