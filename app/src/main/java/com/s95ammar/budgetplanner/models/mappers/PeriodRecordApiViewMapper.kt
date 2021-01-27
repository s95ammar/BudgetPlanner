package com.s95ammar.budgetplanner.models.mappers

import com.s95ammar.budgetplanner.models.api.responses.PeriodRecordApiEntity
import com.s95ammar.budgetplanner.models.view.PeriodRecordViewEntity

object PeriodRecordApiViewMapper : BaseApiViewMapper<PeriodRecordViewEntity, PeriodRecordApiEntity> {

    override fun toViewEntity(apiEntity: PeriodRecordApiEntity?): PeriodRecordViewEntity? {
        if (
            apiEntity?.categoryName == null
            || apiEntity.categoryId == null
            || apiEntity.amount == null
            || apiEntity.isSelected == null
        ) return null

        return PeriodRecordViewEntity(
            categoryName = apiEntity.categoryName,
            categoryId = apiEntity.categoryId,
            max = apiEntity.max,
            amount = apiEntity.amount,
            isSelected = apiEntity.isSelected
        )
    }

}