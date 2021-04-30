package com.s95ammar.budgetplanner.ui.appscreens.dashboard.common.data

import com.s95ammar.budgetplanner.models.BaseApiViewMapper
import com.s95ammar.budgetplanner.models.datasource.remote.api.responses.PeriodSimpleApiEntity

data class PeriodSimpleViewEntity(
    val id: Int,
    val name: String,
    val max: Int?
) {
    object ApiMapper : BaseApiViewMapper<PeriodSimpleViewEntity, PeriodSimpleApiEntity> {

        override fun toViewEntity(apiEntity: PeriodSimpleApiEntity?): PeriodSimpleViewEntity? {
            if (apiEntity?.id == null || apiEntity.name == null) return null

            return PeriodSimpleViewEntity(
                id = apiEntity.id,
                name = apiEntity.name,
                max = apiEntity.max
            )
        }
    }
}