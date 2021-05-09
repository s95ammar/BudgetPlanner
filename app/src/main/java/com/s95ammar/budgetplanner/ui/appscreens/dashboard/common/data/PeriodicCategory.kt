package com.s95ammar.budgetplanner.ui.appscreens.dashboard.common.data

import android.os.Parcelable
import com.s95ammar.budgetplanner.models.BaseEntityMapper
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.PeriodicCategoryEntity
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.join.PeriodicCategoryJoinEntity
import com.s95ammar.budgetplanner.util.INVALID
import kotlinx.parcelize.Parcelize

@Parcelize
data class PeriodicCategory(
    val id: Int,
    val periodId: Int,
    val categoryId: Int,
    val categoryName: String,
    var max: Int?,
    val budgetTransactionsAmountSum: Int,
    val isSelected: Boolean
) : Parcelable {
    object JoinEntityMapper : BaseEntityMapper<PeriodicCategory, PeriodicCategoryJoinEntity> {

        override fun fromEntity(entity: PeriodicCategoryJoinEntity?): PeriodicCategory? {
            return entity?.let {
                PeriodicCategory(
                    it.periodicCategoryId,
                    it.periodId,
                    it.categoryId,
                    it.categoryName,
                    it.max,
                    it.budgetTransactionsAmountSum,
                    isSelected = it.periodicCategoryId != Int.INVALID
                )
            }
        }
    }

    object EntityMapper : BaseEntityMapper<PeriodicCategory, PeriodicCategoryEntity> {

        override fun toEntity(domainObj: PeriodicCategory?): PeriodicCategoryEntity? {
            return domainObj?.let {
                PeriodicCategoryEntity(it.max, it.categoryId, it.periodId).apply { id = it.id }
            }

        }
    }
}
