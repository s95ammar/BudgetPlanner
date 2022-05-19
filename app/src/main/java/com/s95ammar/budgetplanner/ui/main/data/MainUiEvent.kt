package com.s95ammar.budgetplanner.ui.main.data

import com.s95ammar.budgetplanner.ui.common.LoadingState

sealed class MainUiEvent {
    class DisplayLoadingState(val loadingState: LoadingState) : MainUiEvent()
    object NavigateToMainCurrencySelection : MainUiEvent()
    object FinishActivity : MainUiEvent()
}
