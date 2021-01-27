package com.s95ammar.budgetplanner.models.mappers

import com.s95ammar.budgetplanner.models.api.responses.PeriodSimpleApiEntity
import com.s95ammar.budgetplanner.models.view.PeriodSimpleViewEntity

object PeriodSimpleApiViewMapper : BaseApiViewMapper<PeriodSimpleViewEntity, PeriodSimpleApiEntity> {

    override fun toViewEntity(apiEntity: PeriodSimpleApiEntity?): PeriodSimpleViewEntity? {
        if (apiEntity?.id == null || apiEntity.name == null) return null

        return PeriodSimpleViewEntity(
            id = apiEntity.id,
            name = apiEntity.name,
            max = apiEntity.max
        )
    }
}