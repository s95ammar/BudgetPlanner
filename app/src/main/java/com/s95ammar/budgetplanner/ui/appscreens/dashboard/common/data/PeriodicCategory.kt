package com.s95ammar.budgetplanner.ui.appscreens.dashboard.common.data

import com.s95ammar.budgetplanner.models.BaseEntityMapper
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.join.PeriodicCategoryJoinEntity
import com.s95ammar.budgetplanner.util.INVALID

data class PeriodicCategory(
    val id: Int,
    val categoryId: Int,
    val categoryName: String,
    var max: Int?,
    val budgetTransactionsAmountSum: Int,
    val isSelected: Boolean
) {
    object Mapper : BaseEntityMapper<PeriodicCategory, PeriodicCategoryJoinEntity> {

        override fun fromEntity(entity: PeriodicCategoryJoinEntity?): PeriodicCategory? {
            return entity?.let {
                PeriodicCategory(
                    it.periodicCategoryId,
                    it.categoryId,
                    it.categoryName,
                    it.max,
                    it.budgetTransactionsAmountSum,
                    isSelected = it.periodicCategoryId != Int.INVALID
                )
            }
        }

    }
}
