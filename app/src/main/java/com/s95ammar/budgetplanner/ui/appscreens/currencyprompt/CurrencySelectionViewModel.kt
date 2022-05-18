package com.s95ammar.budgetplanner.ui.appscreens.currencyprompt

import androidx.lifecycle.ViewModel
import com.s95ammar.budgetplanner.models.repository.CurrencyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CurrencySelectionViewModel @Inject constructor(
    val currencyRepository: CurrencyRepository
) : ViewModel() {

}