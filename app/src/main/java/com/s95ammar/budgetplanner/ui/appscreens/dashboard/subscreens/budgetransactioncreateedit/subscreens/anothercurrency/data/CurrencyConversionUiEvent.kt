package com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgetransactioncreateedit.subscreens.anothercurrency.data

import com.s95ammar.budgetplanner.ui.common.LoadingState

sealed class CurrencyConversionUiEvent {
    class DisplayLoadingState(val loadingState: LoadingState) : CurrencyConversionUiEvent()
    class NavigateToCurrencySelection(val currentCurrencyCode: String?) : CurrencyConversionUiEvent()
    class SetResult(val amount: Double) : CurrencyConversionUiEvent()
    object Exit: CurrencyConversionUiEvent()
}
