package com.s95ammar.budgetplanner.models.mappers

import com.s95ammar.budgetplanner.models.api.responses.BudgetTransactionApiEntity
import com.s95ammar.budgetplanner.models.view.BudgetTransactionViewEntity

object BudgetTransactionApiViewMapper: BaseApiViewMapper<BudgetTransactionViewEntity, BudgetTransactionApiEntity> {

    override fun toViewEntity(apiEntity: BudgetTransactionApiEntity?): BudgetTransactionViewEntity? {

        if (apiEntity?.id == null
            || apiEntity.name == null
            || apiEntity.type == null
            || apiEntity.amount == null
            || apiEntity.creationUnixMs == null
        ) return null

        return BudgetTransactionViewEntity(
            id = apiEntity.id,
            name = apiEntity.name,
            type = apiEntity.type,
            amount = apiEntity.amount,
            creationUnixMs = apiEntity.creationUnixMs
        )
    }
}