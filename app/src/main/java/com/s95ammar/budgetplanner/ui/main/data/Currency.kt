package com.s95ammar.budgetplanner.ui.main.data

import android.os.Parcelable
import com.s95ammar.budgetplanner.models.BaseDtoMapper
import com.s95ammar.budgetplanner.models.BaseEntityMapper
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.CurrencyEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class Currency(
    val code: String,
    val name: String
) : Parcelable {

    object EntityMapper : BaseEntityMapper<Currency, CurrencyEntity> {
        override fun fromEntity(entity: CurrencyEntity?): Currency? {
            return entity?.let {
                Currency(it.code, it.name)
            }
        }

        override fun toEntity(domainObj: Currency?): CurrencyEntity? {
            return domainObj?.let {
                CurrencyEntity(it.code, it.name)
            }
        }
    }

    object DtoMapper : BaseDtoMapper<Currency, Map.Entry<String, String>> {
        override fun fromDto(dto: Map.Entry<String, String>?): Currency? {
            return dto?.let {
                Currency(it.key, it.value)
            }
        }
    }
}
