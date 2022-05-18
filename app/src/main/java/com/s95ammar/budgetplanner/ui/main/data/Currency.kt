package com.s95ammar.budgetplanner.ui.main.data

import com.s95ammar.budgetplanner.models.BaseEntityMapper
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.CurrencyEntity

data class Currency(
    val code: String,
    val name: String
) {

    object EntityMapper : BaseEntityMapper<Currency, CurrencyEntity> {
        override fun fromEntity(entity: CurrencyEntity?): Currency? {
            return entity?.let {
                Currency(it.code, it.name)
            }
        }
    }
}
