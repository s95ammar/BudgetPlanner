package com.s95ammar.budgetplanner.ui.main.data

import com.s95ammar.budgetplanner.util.CurrencyRatesMap
import com.s95ammar.budgetplanner.util.Optional

class CurrencyDetails(
    val currencyCode: String,
    val ratesOptional: Optional<CurrencyRatesMap>
)
