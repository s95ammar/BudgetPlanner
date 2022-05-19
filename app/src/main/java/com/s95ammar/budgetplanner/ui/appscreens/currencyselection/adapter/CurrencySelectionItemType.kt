package com.s95ammar.budgetplanner.ui.appscreens.currencyselection.adapter

import com.s95ammar.budgetplanner.ui.common.LoadingState
import com.s95ammar.budgetplanner.ui.common.data.Selectable
import com.s95ammar.budgetplanner.ui.main.data.Currency

sealed class CurrencySelectionItemType {
    data class ListItem(val selectableCurrency: Selectable<Currency>): CurrencySelectionItemType()
    data class LoadMore(val loadingState: LoadingState = LoadingState.Cold): CurrencySelectionItemType()
}
