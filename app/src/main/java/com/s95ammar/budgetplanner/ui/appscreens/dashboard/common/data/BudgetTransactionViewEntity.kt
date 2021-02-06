package com.s95ammar.budgetplanner.ui.appscreens.dashboard.common.data

import com.s95ammar.budgetplanner.models.BaseApiViewMapper
import com.s95ammar.budgetplanner.models.api.responses.BudgetTransactionApiEntity
import com.s95ammar.budgetplanner.models.common.IntBudgetTransactionType

data class BudgetTransactionViewEntity(
    val id: Int,
    val name: String,
    @IntBudgetTransactionType val type: Int,
    val amount: Int,
    val creationUnixMs: Long
) {

    object ApiMapper: BaseApiViewMapper<BudgetTransactionViewEntity, BudgetTransactionApiEntity> {

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

        fun List<BudgetTransactionApiEntity?>.mapToViewEntityList() = mapNotNull { toViewEntity(it) }

    }
}