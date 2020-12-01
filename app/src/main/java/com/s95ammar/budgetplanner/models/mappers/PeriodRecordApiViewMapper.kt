package com.s95ammar.budgetplanner.models.mappers

import com.s95ammar.budgetplanner.models.api.responses.PeriodRecordApiEntity
import com.s95ammar.budgetplanner.models.view.PeriodRecordViewEntity

object PeriodRecordApiViewMapper: BaseApiViewMapper<PeriodRecordViewEntity, PeriodRecordApiEntity> {

    override fun toViewEntity(apiEntity: PeriodRecordApiEntity): PeriodRecordViewEntity? {

        if (apiEntity.id == null || apiEntity.amount == null || apiEntity.categoryId == null || apiEntity.periodId == null)
            return null

        return PeriodRecordViewEntity(
            id = apiEntity.id,
            max = apiEntity.max,
            amount = apiEntity.amount,
            categoryId = apiEntity.categoryId,
            periodId = apiEntity.periodId,
        )
    }
}