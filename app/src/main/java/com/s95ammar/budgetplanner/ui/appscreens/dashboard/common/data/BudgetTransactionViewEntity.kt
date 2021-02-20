package com.s95ammar.budgetplanner.ui.appscreens.dashboard.common.data

import com.s95ammar.budgetplanner.models.BaseApiViewMapper
import com.s95ammar.budgetplanner.models.api.responses.BudgetTransactionApiEntity
import com.s95ammar.budgetplanner.models.api.responses.IntBudgetTransactionType

data class BudgetTransactionViewEntity(
    val id: Int,
    val name: String,
    @IntBudgetTransactionType val type: Int,
    val amount: Int,
    val creationUnixMs: Long,
    val periodicCategoryId: Int,
    val categoryName: String
) {

    object ApiMapper: BaseApiViewMapper<BudgetTransactionViewEntity, BudgetTransactionApiEntity> {

        override fun toViewEntity(apiEntity: BudgetTransactionApiEntity?): BudgetTransactionViewEntity? {

            if (apiEntity?.id == null
                || apiEntity.name == null
                || apiEntity.type == null
                || apiEntity.amount == null
                || apiEntity.creationUnixMs == null
                || apiEntity.periodicCategoryId == null
                || apiEntity.categoryName == null
            ) return null

            return BudgetTransactionViewEntity(
                id = apiEntity.id,
                name = apiEntity.name,
                type = apiEntity.type,
                amount = apiEntity.amount,
                creationUnixMs = apiEntity.creationUnixMs,
                periodicCategoryId = apiEntity.periodicCategoryId,
                categoryName = apiEntity.categoryName
            )
        }

        fun List<BudgetTransactionApiEntity?>.mapToViewEntityList() = mapNotNull { toViewEntity(it) }

    }
}