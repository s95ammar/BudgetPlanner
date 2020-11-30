package com.s95ammar.budgetplanner.models.mappers

import com.s95ammar.budgetplanner.models.api.common.PeriodApiEntity
import com.s95ammar.budgetplanner.models.view.PeriodViewEntity
import com.s95ammar.budgetplanner.util.NO_ITEM

object PeriodApiViewMapper : BaseApiViewMapper<PeriodViewEntity, PeriodApiEntity> {

    override fun toViewEntity(apiEntity: PeriodApiEntity): PeriodViewEntity {
        return PeriodViewEntity(
            id = apiEntity.id ?: Int.NO_ITEM,
            name = apiEntity.name.orEmpty(),
            max = apiEntity.max
        )
    }

}