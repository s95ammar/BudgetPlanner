package com.s95ammar.budgetplanner.ui.appscreens.dashboard.subscreens.budgetransactioncreateedit.subscreens.anothercurrency.data

import com.s95ammar.budgetplanner.models.BaseDtoMapper
import com.s95ammar.budgetplanner.models.datasource.remote.api.dto.ConversionDto
import com.s95ammar.budgetplanner.util.orDefault

typealias CurrencyRatesMap = Map<String, Double>

object CurrencyRatesMapDtoMapper : BaseDtoMapper<CurrencyRatesMap, ConversionDto> {

    override fun fromDto(dto: ConversionDto?): CurrencyRatesMap? {
        return dto?.rates?.map { it.key to 1 / it.value.rate.orDefault(1.0) }?.toMap()
    }
}
