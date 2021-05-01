package com.s95ammar.budgetplanner.ui.appscreens.dashboard.common.data

import com.s95ammar.budgetplanner.models.BaseEntityMapper
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.PeriodEntity

data class PeriodSimple(
    val id: Int,
    val name: String,
    val max: Int?
) {
    object Mapper : BaseEntityMapper<PeriodSimple, PeriodEntity> {

        override fun fromEntity(entity: PeriodEntity?): PeriodSimple? {
            return entity?.let {
                PeriodSimple(it.id, it.name, it.max)
            }
        }

    }
}