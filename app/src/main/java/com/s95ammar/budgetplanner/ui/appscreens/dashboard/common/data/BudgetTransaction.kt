package com.s95ammar.budgetplanner.ui.appscreens.dashboard.common.data

import com.s95ammar.budgetplanner.models.BaseEntityMapper
import com.s95ammar.budgetplanner.models.IntBudgetTransactionType
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.join.BudgetTransactionJoinEntity

data class BudgetTransaction(
    val id: Int,
    val name: String,
    @IntBudgetTransactionType val type: Int,
    val amount: Int,
    val creationUnixMs: Long,
    val periodicCategoryId: Int,
    val categoryName: String
) {
    object Mapper : BaseEntityMapper<BudgetTransaction, BudgetTransactionJoinEntity> {

        override fun fromEntity(entity: BudgetTransactionJoinEntity?): BudgetTransaction? {
            return entity?.let {
                BudgetTransaction(
                    it.id,
                    it.name,
                    it.type,
                    it.amount,
                    it.creationUnixMs,
                    it.periodicCategoryId,
                    it.categoryName
                )
            }
        }

    }
}