package com.s95ammar.budgetplanner.models.mappers

import com.s95ammar.budgetplanner.models.api.responses.PeriodApiEntity
import com.s95ammar.budgetplanner.models.view.PeriodViewEntity

object PeriodApiViewMapper : BaseApiViewMapper<PeriodViewEntity, PeriodApiEntity> {

    override fun toViewEntity(apiEntity: PeriodApiEntity?): PeriodViewEntity? {
        if (apiEntity?.id == null || apiEntity.name == null) return null

        return PeriodViewEntity(
            id = apiEntity.id,
            name = apiEntity.name,
            max = apiEntity.max,
            periodRecords = apiEntity.periodRecords.orEmpty().mapNotNull(PeriodRecordApiViewMapper::toViewEntity),
            budgetTransactions = apiEntity.budgetTransactions.orEmpty().mapNotNull(BudgetTransactionApiViewMapper::toViewEntity)
        )
    }
}