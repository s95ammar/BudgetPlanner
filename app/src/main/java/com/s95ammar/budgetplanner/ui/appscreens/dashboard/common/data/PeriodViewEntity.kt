package com.s95ammar.budgetplanner.ui.appscreens.dashboard.common.data

import com.s95ammar.budgetplanner.models.BaseApiViewMapper
import com.s95ammar.budgetplanner.models.datasource.remote.api.responses.PeriodApiEntity

data class PeriodViewEntity(
    val id: Int,
    val name: String,
    val max: Int?,
    val periodicCategories: List<PeriodicCategoryViewEntity>,
    val budgetTransactions: List<BudgetTransactionViewEntity>
) {
    object ApiMapper : BaseApiViewMapper<PeriodViewEntity, PeriodApiEntity> {

        override fun toViewEntity(apiEntity: PeriodApiEntity?): PeriodViewEntity? {
            if (apiEntity?.id == null || apiEntity.name == null) return null

            return PeriodViewEntity(
                id = apiEntity.id,
                name = apiEntity.name,
                max = apiEntity.max,
                periodicCategories = apiEntity.periodicCategories.orEmpty().mapNotNull(PeriodicCategoryViewEntity.ApiMapper::toViewEntity),
                budgetTransactions = apiEntity.budgetTransactions.orEmpty().mapNotNull(BudgetTransactionViewEntity.ApiMapper::toViewEntity)
            )
        }
    }
}