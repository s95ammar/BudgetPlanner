package com.s95ammar.budgetplanner.ui.main.data

sealed class MainUiEvent {
    object NavigateToCurrencySelection : MainUiEvent()
    object FinishActivity : MainUiEvent()
}
